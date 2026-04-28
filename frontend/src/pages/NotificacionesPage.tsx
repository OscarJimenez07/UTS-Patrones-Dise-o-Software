import { useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { MessageSquare, Mail, Send, Loader2, Trophy } from 'lucide-react'

export function NotificacionesPage() {
  const [canal, setCanal] = useState<'discord' | 'correo'>('discord')
  const [mensaje, setMensaje] = useState('Te quedan 3 minutos para entrar a tu partida!')
  const [log, setLog] = useState<string | undefined>()
  const [loading, setLoading] = useState(false)
  const [modo, setModo] = useState('Equipos 3v3')
  const [victoria, setVictoria] = useState(true)

  async function enviarAlerta() {
    setLoading(true)
    try {
      const u = auth.get()
      const r = await api.post<{ log: string }>('/notificaciones/enviar', {
        usuario: u?.usuario,
        mensaje,
        canal,
      })
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  async function enviarResultado() {
    setLoading(true)
    try {
      const u = auth.get()
      const r = await api.post<{ log: string }>('/notificaciones/resultado', {
        usuario: u?.usuario,
        modo,
        victoria,
        canal,
      })
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Notificaciones</h1>
        <PatternBadge
          patron="Adapter"
          description="ServicioDiscord y ServicioCorreo tienen interfaces incompatibles. Los adapters los traducen a la interfaz NotificacionJuego que el motor de juegos espera."
        />
      </div>

      <div className="panel p-5 space-y-4">
        <label className="label">Canal</label>
        <div className="grid grid-cols-2 gap-3">
          <button
            onClick={() => setCanal('discord')}
            className={`p-4 rounded-lg border transition flex items-center gap-3 ${
              canal === 'discord'
                ? 'bg-fuchsia-600/20 border-fuchsia-400/40 text-white'
                : 'bg-ink-800/60 border-white/5 text-slate-400'
            }`}
          >
            <MessageSquare size={18} />
            <div className="text-left">
              <p className="font-semibold">Discord</p>
              <p className="text-xs opacity-70">AdaptadorDiscord → ServicioDiscord</p>
            </div>
          </button>
          <button
            onClick={() => setCanal('correo')}
            className={`p-4 rounded-lg border transition flex items-center gap-3 ${
              canal === 'correo'
                ? 'bg-cyan-600/20 border-cyan-400/40 text-white'
                : 'bg-ink-800/60 border-white/5 text-slate-400'
            }`}
          >
            <Mail size={18} />
            <div className="text-left">
              <p className="font-semibold">Correo</p>
              <p className="text-xs opacity-70">AdaptadorCorreo → ServicioCorreo</p>
            </div>
          </button>
        </div>

        <div>
          <label className="label">Mensaje de alerta</label>
          <textarea
            className="input min-h-[80px]"
            value={mensaje}
            onChange={(e) => setMensaje(e.target.value)}
          />
        </div>

        <div className="flex flex-wrap gap-3">
          <button className="btn-primary" onClick={enviarAlerta} disabled={loading || !mensaje}>
            {loading ? <Loader2 className="animate-spin" size={14} /> : <Send size={14} />}
            Enviar alerta
          </button>
        </div>

        <div className="border-t border-white/5 pt-4 space-y-3">
          <p className="text-sm text-slate-400">Simular resultado de partida</p>
          <div className="flex gap-3 items-center">
            <input
              className="input flex-1"
              value={modo}
              onChange={(e) => setModo(e.target.value)}
              placeholder="Equipos 3v3"
            />
            <label className="flex items-center gap-2 text-sm text-slate-300">
              <input
                type="checkbox"
                checked={victoria}
                onChange={(e) => setVictoria(e.target.checked)}
                className="accent-emerald-500"
              />
              Victoria
            </label>
            <button className="btn-ghost" onClick={enviarResultado} disabled={loading}>
              <Trophy size={14} /> Reportar
            </button>
          </div>
        </div>
      </div>

      <Terminal output={log} />
    </div>
  )
}
