import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Target, RefreshCcw, Gift, Plus, Loader2 } from 'lucide-react'

type Mision = {
  id: string
  titulo: string
  descripcion: string
  recompensa: string
  progreso: number
  meta: number
  estado: 'Disponible' | 'EnProgreso' | 'Completada' | 'Reclamada' | 'Expirada'
}

const ESTADO_COLOR: Record<Mision['estado'], string> = {
  Disponible: 'border-slate-400/40 bg-slate-400/10 text-slate-300',
  EnProgreso: 'border-cyan-400/40 bg-cyan-400/10 text-cyan-300',
  Completada: 'border-amber-400/40 bg-amber-400/10 text-amber-300',
  Reclamada: 'border-emerald-400/40 bg-emerald-400/10 text-emerald-300',
  Expirada: 'border-rose-400/40 bg-rose-400/10 text-rose-300',
}

export function MisionesPage() {
  const user = auth.get()
  const usuario = user?.usuario ?? 'oscar'
  const [misiones, setMisiones] = useState<Mision[]>([])
  const [log, setLog] = useState<string | undefined>()
  const [loading, setLoading] = useState(false)

  async function refrescar() {
    const r = await api.get<{ misiones: Mision[] }>(`/misiones/${usuario}`)
    setMisiones(r.misiones ?? [])
  }

  useEffect(() => {
    refrescar()
  }, [])

  async function avanzar(id: string) {
    setLoading(true)
    try {
      const r = await api.post<{ misiones: Mision[]; log: string }>(
        `/misiones/${usuario}/${id}/progreso`,
        { delta: 1 },
      )
      if (r.misiones) setMisiones(r.misiones)
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  async function reclamar(id: string) {
    setLoading(true)
    try {
      const r = await api.post<{ misiones: Mision[]; log: string }>(
        `/misiones/${usuario}/${id}/reclamar`,
      )
      if (r.misiones) setMisiones(r.misiones)
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  async function regenerar() {
    setLoading(true)
    try {
      const r = await api.post<{ misiones: Mision[]; log: string }>(
        `/misiones/${usuario}/refrescar`,
      )
      if (r.misiones) setMisiones(r.misiones)
      setLog(r.log)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Misiones diarias</h1>
        <PatternBadge
          patron="State"
          description="Cada misión transita entre Disponible → EnProgreso → Completada → Reclamada. El propio estado decide qué transiciones son válidas; el contexto (Mision) solo delega."
        />
      </div>

      <div className="panel p-5 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Target size={28} className="text-emerald-400" />
          <div>
            <p className="font-display font-bold text-white">Misiones del día</p>
            <p className="text-xs text-slate-400">3 misiones diarias por jugador</p>
          </div>
        </div>
        <button className="btn-ghost" onClick={regenerar} disabled={loading}>
          {loading ? <Loader2 className="animate-spin" size={14} /> : <RefreshCcw size={14} />}
          Regenerar (expira las actuales)
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {misiones.map((m) => {
          const pct = Math.min(100, (m.progreso / Math.max(m.meta, 1)) * 100)
          const puedeAvanzar = m.estado === 'Disponible' || m.estado === 'EnProgreso'
          const puedeReclamar = m.estado === 'Completada'
          return (
            <div key={m.id} className="panel p-5 space-y-3">
              <div className="flex items-start justify-between gap-2">
                <p className="font-display font-bold text-white">{m.titulo}</p>
                <span className={`pattern-badge ${ESTADO_COLOR[m.estado]}`}>{m.estado}</span>
              </div>
              <p className="text-xs text-slate-400">{m.descripcion}</p>
              <div className="space-y-1">
                <div className="h-2 bg-white/5 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-cyan-500 to-emerald-400"
                    style={{ width: `${pct}%` }}
                  />
                </div>
                <p className="text-xs text-slate-500">
                  {m.progreso} / {m.meta}
                </p>
              </div>
              <p className="text-xs text-emerald-300">🎁 {m.recompensa}</p>
              <div className="flex gap-2">
                <button
                  className="btn-ghost flex-1 justify-center text-xs"
                  onClick={() => avanzar(m.id)}
                  disabled={loading || !puedeAvanzar}
                >
                  <Plus size={12} />
                  +1 progreso
                </button>
                <button
                  className="btn-primary flex-1 justify-center text-xs"
                  onClick={() => reclamar(m.id)}
                  disabled={loading || !puedeReclamar}
                >
                  <Gift size={12} />
                  Reclamar
                </button>
              </div>
            </div>
          )
        })}
      </div>

      <Terminal output={log} />
    </div>
  )
}
