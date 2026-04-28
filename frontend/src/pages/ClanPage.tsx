import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { ChevronRight, ChevronDown, User, Users, Plus, Send, Loader2 } from 'lucide-react'

type Nodo = {
  nombre: string
  poder: number
  miembros: number
  tipo: 'escuadron' | 'jugador'
  rol?: string
  hijos?: Nodo[]
}

type ClanResp = {
  clan: Nodo
  escuadrones: string[]
}

export function ClanPage() {
  const [data, setData] = useState<ClanResp | null>(null)
  const [log, setLog] = useState<string | undefined>()
  const [loading, setLoading] = useState(false)

  // form agregar jugador
  const [nombre, setNombre] = useState('')
  const [poder, setPoder] = useState(1000)
  const [escuadron, setEscuadron] = useState('')
  const [rol, setRol] = useState('Miembro')

  // form mensaje
  const [mensajeEscuadron, setMensajeEscuadron] = useState('')
  const [mensaje, setMensaje] = useState('Reunión en el lobby a las 8pm!')

  async function refrescar() {
    const r = await api.get<ClanResp>('/clan')
    setData(r as unknown as ClanResp)
  }

  useEffect(() => {
    refrescar()
  }, [])

  async function enviarMensaje() {
    setLoading(true)
    try {
      const r = await api.post<{ log: string }>('/clan/mensaje', {
        escuadron: mensajeEscuadron || null,
        mensaje,
      })
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  async function agregarJugador() {
    if (!nombre.trim()) return
    setLoading(true)
    try {
      const r = await api.post<{ ok: boolean }>('/clan/jugador', {
        escuadron: escuadron || null,
        nombre,
        poder,
        rol,
      })
      if (r.ok) {
        setNombre('')
        await refrescar()
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Clan y escuadrones</h1>
        <PatternBadge
          patron="Composite"
          description="Un escuadrón puede contener jugadores y otros escuadrones. Las operaciones (poder, contar miembros, mensaje) se propagan recursivamente."
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        <div className="panel p-5 lg:col-span-2">
          <h3 className="font-semibold mb-3 flex items-center gap-2">
            <Users size={16} /> Jerarquía del clan
          </h3>
          {data ? <Tree nodo={data.clan} nivel={0} /> : <p className="text-slate-500">Cargando...</p>}
        </div>

        <div className="space-y-5">
          <div className="panel p-5 space-y-3">
            <h3 className="font-semibold flex items-center gap-2">
              <Plus size={16} /> Agregar jugador
            </h3>
            <input
              className="input"
              placeholder="Nombre"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
            />
            <div>
              <label className="label">Poder: {poder}</label>
              <input
                type="range"
                min={100}
                max={5000}
                value={poder}
                onChange={(e) => setPoder(Number(e.target.value))}
                className="w-full accent-violet-500"
              />
            </div>
            <select
              className="input"
              value={escuadron}
              onChange={(e) => setEscuadron(e.target.value)}
            >
              <option value="">Directamente al clan</option>
              {data?.escuadrones.map((es) => (
                <option key={es} value={es}>
                  Escuadrón {es}
                </option>
              ))}
            </select>
            <input
              className="input"
              placeholder="Rol"
              value={rol}
              onChange={(e) => setRol(e.target.value)}
            />
            <button
              className="btn-primary w-full justify-center"
              onClick={agregarJugador}
              disabled={loading || !nombre}
            >
              {loading ? <Loader2 className="animate-spin" size={14} /> : <Plus size={14} />}
              Agregar al composite
            </button>
          </div>

          <div className="panel p-5 space-y-3">
            <h3 className="font-semibold flex items-center gap-2">
              <Send size={16} /> Enviar mensaje
            </h3>
            <select
              className="input"
              value={mensajeEscuadron}
              onChange={(e) => setMensajeEscuadron(e.target.value)}
            >
              <option value="">A todo el clan (raíz del composite)</option>
              {data?.escuadrones.map((es) => (
                <option key={es} value={es}>
                  Solo a Escuadrón {es}
                </option>
              ))}
            </select>
            <textarea
              className="input min-h-[60px]"
              value={mensaje}
              onChange={(e) => setMensaje(e.target.value)}
            />
            <button
              className="btn-primary w-full justify-center"
              onClick={enviarMensaje}
              disabled={loading}
            >
              {loading ? <Loader2 className="animate-spin" size={14} /> : <Send size={14} />}
              Difundir mensaje
            </button>
          </div>
        </div>
      </div>

      <Terminal output={log} />
    </div>
  )
}

function Tree({ nodo, nivel }: { nodo: Nodo; nivel: number }) {
  const [open, setOpen] = useState(true)
  const isGroup = nodo.tipo === 'escuadron'

  return (
    <div style={{ marginLeft: nivel * 16 }} className="my-1">
      <div className="flex items-center gap-2">
        {isGroup ? (
          <button
            onClick={() => setOpen(!open)}
            className="text-slate-400 hover:text-white"
          >
            {open ? <ChevronDown size={14} /> : <ChevronRight size={14} />}
          </button>
        ) : (
          <span className="w-[14px]" />
        )}
        {isGroup ? (
          <Users size={14} className="text-violet-400" />
        ) : (
          <User size={14} className="text-emerald-400" />
        )}
        <span className={isGroup ? 'font-semibold text-white' : 'text-slate-200'}>
          {nodo.nombre}
        </span>
        {nodo.rol && (
          <span className="text-xs text-slate-500">[{nodo.rol}]</span>
        )}
        <span className="ml-auto flex items-center gap-3 text-xs text-slate-400">
          {isGroup && <span>{nodo.miembros} miembros</span>}
          <span className="text-amber-300 font-mono">⚡ {nodo.poder}</span>
        </span>
      </div>
      {isGroup && open && nodo.hijos && (
        <div className="border-l border-white/5 ml-2 mt-1 pl-2">
          {nodo.hijos.map((h, i) => (
            <Tree key={i} nodo={h} nivel={nivel + 1} />
          ))}
        </div>
      )}
    </div>
  )
}
