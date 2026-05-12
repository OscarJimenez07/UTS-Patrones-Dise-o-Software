import { useEffect, useRef, useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { MessageSquare, Send, Users, Loader2 } from 'lucide-react'

type Mensaje = {
  canal: string
  remitente: string
  contenido: string
  timestamp: number
}

const CANALES = [
  { clave: 'global', nombre: 'Global', desc: 'Visible para todos los conectados' },
  { clave: 'clan', nombre: 'Clan', desc: 'Solo miembros del clan' },
]

export function ChatPage() {
  const user = auth.get()
  const usuario = user?.usuario ?? 'oscar'
  const [canal, setCanal] = useState('global')
  const [mensajes, setMensajes] = useState<Mensaje[]>([])
  const [conectados, setConectados] = useState(0)
  const [texto, setTexto] = useState('')
  const [log, setLog] = useState<string | undefined>()
  const [loading, setLoading] = useState(false)
  const scrollRef = useRef<HTMLDivElement>(null)

  async function refrescar() {
    const r = await api.get<{ mensajes: Mensaje[]; usuariosConectados: number }>(
      `/chat/${canal}`,
    )
    setMensajes(r.mensajes ?? [])
    setConectados(r.usuariosConectados ?? 0)
  }

  useEffect(() => {
    refrescar()
  }, [canal])

  useEffect(() => {
    scrollRef.current?.scrollTo({ top: scrollRef.current.scrollHeight })
  }, [mensajes])

  async function enviar() {
    if (!texto.trim()) return
    setLoading(true)
    try {
      const r = await api.post<{ mensajes: Mensaje[]; usuariosConectados: number; log: string }>(
        '/chat/enviar',
        { usuario, canal, mensaje: texto },
      )
      setMensajes(r.mensajes ?? [])
      setConectados(r.usuariosConectados ?? 0)
      setLog(r.log)
      setTexto('')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Chat en tiempo real</h1>
        <PatternBadge
          patron="Mediator"
          description="La SalaChat centraliza la comunicación entre jugadores. Nadie tiene referencias directas a los demás — todo pasa por el mediador, que aplica filtrado y propaga al canal correcto."
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-5">
        <div className="space-y-3">
          <h3 className="font-semibold text-sm flex items-center gap-2">
            <MessageSquare size={16} /> Canales
          </h3>
          {CANALES.map((c) => (
            <button
              key={c.clave}
              onClick={() => setCanal(c.clave)}
              className={`w-full panel p-3 text-left transition ${
                canal === c.clave ? 'border-fuchsia-400/40 shadow-neon-violet' : ''
              }`}
            >
              <p className="font-semibold">#{c.nombre}</p>
              <p className="text-xs text-slate-400 mt-1">{c.desc}</p>
            </button>
          ))}
          <div className="panel p-3 flex items-center gap-2 text-sm">
            <Users size={14} className="text-emerald-400" />
            <span className="text-slate-300">{conectados} conectados</span>
          </div>
        </div>

        <div className="panel p-5 lg:col-span-3 flex flex-col">
          <div className="flex items-center justify-between mb-3">
            <h3 className="font-semibold">#{canal}</h3>
            <p className="text-xs text-slate-500">Mediator activo</p>
          </div>

          <div
            ref={scrollRef}
            className="flex-1 min-h-[300px] max-h-[450px] overflow-auto space-y-2 pr-2"
          >
            {mensajes.length === 0 && (
              <p className="text-slate-500 text-sm">Sin mensajes en este canal todavía.</p>
            )}
            {mensajes.map((m, i) => {
              const yo = m.remitente.toLowerCase() === usuario.toLowerCase()
              return (
                <div
                  key={i}
                  className={`flex ${yo ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[70%] panel p-3 ${
                      yo
                        ? 'border-violet-400/30 bg-violet-500/10'
                        : 'border-white/5 bg-ink-900/80'
                    }`}
                  >
                    <p className="text-xs text-slate-400 font-semibold">
                      {yo ? 'tú' : m.remitente}
                    </p>
                    <p className="text-sm text-slate-100 mt-1">{m.contenido}</p>
                  </div>
                </div>
              )
            })}
          </div>

          <div className="mt-4 flex gap-2">
            <input
              className="input flex-1"
              placeholder={`Mensaje al canal #${canal}...`}
              value={texto}
              onChange={(e) => setTexto(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') enviar()
              }}
            />
            <button className="btn-primary" onClick={enviar} disabled={loading || !texto.trim()}>
              {loading ? <Loader2 className="animate-spin" size={14} /> : <Send size={14} />}
              Enviar
            </button>
          </div>
        </div>
      </div>

      <Terminal output={log} />
    </div>
  )
}
