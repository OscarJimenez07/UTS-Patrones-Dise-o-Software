import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Map, Users, Clock, Trophy, Loader2, PlayCircle, Copy } from 'lucide-react'

type Modo = {
  clave: string
  modo: string
  maxJugadores: number
  duracionMinutos: number
  mapa: string
  rankedPermitido: boolean
  reconexionPermitida: boolean
}

export function PartidaPage() {
  const [tab, setTab] = useState<'configurar' | 'iniciar'>('configurar')
  const [modos, setModos] = useState<Modo[]>([])
  const [seleccionado, setSeleccionado] = useState<string>('Duelo')
  const [maxJug, setMaxJug] = useState<number>(2)
  const [duracion, setDuracion] = useState<number>(10)
  const [resultadoCrear, setResultadoCrear] = useState<{ config?: Modo; log?: string } | null>(null)
  const [resultadoIniciar, setResultadoIniciar] = useState<{ log?: string } | null>(null)
  const [canal, setCanal] = useState<'discord' | 'correo'>('discord')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    api.get<Modo[]>('/partida/modos').then((r) => {
      setModos(r as unknown as Modo[])
      const inicial = (r as unknown as Modo[]).find((m) => m.clave === 'Duelo')
      if (inicial) {
        setMaxJug(inicial.maxJugadores)
        setDuracion(inicial.duracionMinutos)
      }
    })
  }, [])

  function elegir(clave: string) {
    setSeleccionado(clave)
    const m = modos.find((x) => x.clave === clave)
    if (m) {
      setMaxJug(m.maxJugadores)
      setDuracion(m.duracionMinutos)
    }
  }

  async function clonar() {
    setLoading(true)
    try {
      const r = await api.post<{ config: Modo; log: string }>('/partida/crear', {
        clave: seleccionado,
        maxJugadores: maxJug,
        duracionMinutos: duracion,
      })
      setResultadoCrear(r)
    } finally {
      setLoading(false)
    }
  }

  async function iniciar() {
    setLoading(true)
    try {
      const u = auth.get()
      const r = await api.post<{ log: string }>('/partida/iniciar', {
        usuario: u?.usuario,
        claveModo: seleccionado,
        canal,
      })
      setResultadoIniciar(r)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Partidas</h1>
        <PatternBadge
          patron="Prototype"
          description="Cada modo es un PROTOTIPO clonable. Modificar el clon NO altera el original — pruébalo cambiando los valores y volviendo a abrir el modo."
        />
      </div>

      <div className="grid grid-cols-2 gap-2 p-1 bg-ink-800/60 rounded-lg max-w-md">
        <button
          onClick={() => setTab('configurar')}
          className={`py-2 rounded-md text-sm font-semibold transition flex items-center justify-center gap-2 ${
            tab === 'configurar' ? 'bg-emerald-600/30 text-white' : 'text-slate-400'
          }`}
        >
          <Copy size={14} /> Clonar config
        </button>
        <button
          onClick={() => setTab('iniciar')}
          className={`py-2 rounded-md text-sm font-semibold transition flex items-center justify-center gap-2 ${
            tab === 'iniciar' ? 'bg-violet-600/30 text-white' : 'text-slate-400'
          }`}
        >
          <PlayCircle size={14} /> Iniciar (Facade)
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
        {modos.map((m) => (
          <button
            key={m.clave}
            onClick={() => elegir(m.clave)}
            className={`panel p-4 text-left transition ${
              seleccionado === m.clave ? 'border-emerald-400/50 shadow-neon-emerald' : ''
            }`}
          >
            <p className="font-display font-bold text-white">{m.modo}</p>
            <p className="text-xs text-slate-400 mt-1 flex items-center gap-2">
              <Map size={12} /> {m.mapa}
            </p>
            <div className="flex items-center gap-3 mt-3 text-xs text-slate-300">
              <span className="flex items-center gap-1">
                <Users size={12} />
                {m.maxJugadores}
              </span>
              <span className="flex items-center gap-1">
                <Clock size={12} />
                {m.duracionMinutos}m
              </span>
              {m.rankedPermitido && (
                <span className="flex items-center gap-1 text-amber-300">
                  <Trophy size={12} /> ranked
                </span>
              )}
            </div>
          </button>
        ))}
      </div>

      {tab === 'configurar' && (
        <div className="panel p-5 space-y-4">
          <h3 className="font-semibold text-white">Personaliza el clon</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="label">Máx. jugadores: {maxJug}</label>
              <input
                type="range"
                min={2}
                max={100}
                value={maxJug}
                onChange={(e) => setMaxJug(Number(e.target.value))}
                className="w-full accent-violet-500"
              />
            </div>
            <div>
              <label className="label">Duración (min): {duracion}</label>
              <input
                type="range"
                min={5}
                max={60}
                value={duracion}
                onChange={(e) => setDuracion(Number(e.target.value))}
                className="w-full accent-violet-500"
              />
            </div>
          </div>
          <button className="btn-primary" onClick={clonar} disabled={loading}>
            {loading ? <Loader2 className="animate-spin" size={14} /> : <Copy size={14} />}
            Clonar configuración
          </button>
          {resultadoCrear?.config && (
            <div className="text-sm text-slate-300 grid grid-cols-2 md:grid-cols-3 gap-3 bg-ink-800/60 rounded-lg p-3">
              <div><span className="text-slate-500">Modo:</span> {resultadoCrear.config.modo}</div>
              <div><span className="text-slate-500">Jugadores:</span> {resultadoCrear.config.maxJugadores}</div>
              <div><span className="text-slate-500">Duración:</span> {resultadoCrear.config.duracionMinutos} min</div>
              <div><span className="text-slate-500">Mapa:</span> {resultadoCrear.config.mapa}</div>
              <div><span className="text-slate-500">Ranked:</span> {resultadoCrear.config.rankedPermitido ? 'Sí' : 'No'}</div>
              <div><span className="text-slate-500">Reconexión:</span> {resultadoCrear.config.reconexionPermitida ? 'Sí' : 'No'}</div>
            </div>
          )}
          <Terminal output={resultadoCrear?.log} />
        </div>
      )}

      {tab === 'iniciar' && (
        <div className="panel p-5 space-y-4">
          <PatternBadge
            patron="Facade"
            description="Un solo método orquesta 3 patrones: valida sesión (Singleton), clona configuración (Prototype) y notifica al jugador (Adapter)."
          />
          <div>
            <label className="label">Canal de notificación</label>
            <div className="flex gap-2">
              {(['discord', 'correo'] as const).map((c) => (
                <button
                  key={c}
                  onClick={() => setCanal(c)}
                  className={`px-4 py-2 rounded-lg text-sm transition ${
                    canal === c
                      ? 'bg-fuchsia-600/30 text-white border border-fuchsia-400/40'
                      : 'bg-ink-800/60 text-slate-400 border border-white/5'
                  }`}
                >
                  {c === 'discord' ? 'Discord' : 'Correo'}
                </button>
              ))}
            </div>
          </div>
          <button className="btn-primary" onClick={iniciar} disabled={loading}>
            {loading ? <Loader2 className="animate-spin" size={14} /> : <PlayCircle size={14} />}
            Iniciar partida ({seleccionado})
          </button>
          <Terminal output={resultadoIniciar?.log} />
        </div>
      )}
    </div>
  )
}
