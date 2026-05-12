import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Award, Lock, CheckCircle2, Zap, Loader2 } from 'lucide-react'

type Logro = {
  clave: string
  nombre: string
  descripcion: string
  recompensa: string
  meta: number
  progreso: number
  desbloqueado: boolean
}

const EVENTOS = [
  { tipo: 'PARTIDA_FINALIZADA', label: 'Partida ganada', datos: { victoria: true } },
  { tipo: 'PARTIDA_FINALIZADA', label: 'Partida perdida', datos: { victoria: false } },
  { tipo: 'MENSAJE_CLAN', label: 'Mensaje al clan', datos: {} },
  { tipo: 'COMPRA_REALIZADA', label: 'Compra realizada', datos: {} },
]

export function LogrosPage() {
  const user = auth.get()
  const usuario = user?.usuario ?? 'oscar'
  const [logros, setLogros] = useState<Logro[]>([])
  const [log, setLog] = useState<string | undefined>()
  const [loading, setLoading] = useState(false)

  async function refrescar() {
    const r = await api.get<{ logros: Logro[] }>(`/logros/${usuario}`)
    setLogros(r.logros ?? [])
  }

  useEffect(() => {
    refrescar()
  }, [])

  async function disparar(evento: (typeof EVENTOS)[number]) {
    setLoading(true)
    try {
      const r = await api.post<{ logros: Logro[]; log: string }>('/logros/evento', {
        tipo: evento.tipo,
        usuario,
        datos: evento.datos,
      })
      if (r.logros) setLogros(r.logros)
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  const desbloqueados = logros.filter((l) => l.desbloqueado).length

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Logros y recompensas</h1>
        <PatternBadge
          patron="Observer"
          description="El subject (EventosJuego) publica eventos y cada logro suscrito (observer) decide si avanza su contador y se desbloquea. Permite añadir logros nuevos sin tocar el código que publica eventos."
        />
      </div>

      <div className="panel p-5 flex items-center justify-between">
        <div>
          <p className="text-xs text-slate-500 uppercase tracking-wider">Progreso</p>
          <p className="font-display font-extrabold text-3xl text-white mt-1">
            {desbloqueados} / {logros.length}
          </p>
        </div>
        <Award size={48} className="text-amber-400" />
      </div>

      <div className="panel p-5 space-y-3">
        <h3 className="font-semibold flex items-center gap-2">
          <Zap size={16} /> Disparar evento (Subject publica → Observers reaccionan)
        </h3>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
          {EVENTOS.map((e) => (
            <button
              key={e.label}
              onClick={() => disparar(e)}
              className="btn-ghost justify-center text-xs"
              disabled={loading}
            >
              {loading ? <Loader2 className="animate-spin" size={12} /> : <Zap size={12} />}
              {e.label}
            </button>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {logros.map((l) => {
          const pct = Math.min(100, (l.progreso / Math.max(l.meta, 1)) * 100)
          return (
            <div
              key={l.clave}
              className={`panel p-5 ${
                l.desbloqueado ? 'border-amber-400/40 shadow-neon-cyan' : ''
              }`}
            >
              <div className="flex items-start gap-3">
                {l.desbloqueado ? (
                  <CheckCircle2 size={28} className="text-amber-400" />
                ) : (
                  <Lock size={28} className="text-slate-500" />
                )}
                <div className="flex-1">
                  <p className="font-display font-bold text-white">{l.nombre}</p>
                  <p className="text-xs text-slate-400 mt-1">{l.descripcion}</p>
                  <div className="mt-3 space-y-1">
                    <div className="h-2 bg-white/5 rounded-full overflow-hidden">
                      <div
                        className={`h-full ${
                          l.desbloqueado
                            ? 'bg-gradient-to-r from-amber-400 to-amber-200'
                            : 'bg-gradient-to-r from-violet-500 to-fuchsia-500'
                        }`}
                        style={{ width: `${pct}%` }}
                      />
                    </div>
                    <p className="text-xs text-slate-500">
                      {l.progreso} / {l.meta}
                    </p>
                  </div>
                  <p className="text-xs text-emerald-300 mt-2">🎁 {l.recompensa}</p>
                </div>
              </div>
            </div>
          )
        })}
      </div>

      <Terminal output={log} />
    </div>
  )
}
