import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { History, Trophy, X, Save, Trash2, Loader2 } from 'lucide-react'

type Snapshot = {
  id: string
  usuario: string
  modo: string
  victoria: boolean
  puntuacion: number
  duracionSegundos: number
  mejorasAplicadas: string[]
  resumen: string
  timestamp: number
}

const MODOS = ['Duelo', 'Equipos', 'BattleRoyale']

export function HistorialPage() {
  const user = auth.get()
  const usuario = user?.usuario ?? 'oscar'
  const [snapshots, setSnapshots] = useState<Snapshot[]>([])
  const [seleccion, setSeleccion] = useState<Snapshot | null>(null)
  const [log, setLog] = useState<string | undefined>()
  const [loading, setLoading] = useState(false)

  // form
  const [modo, setModo] = useState('Duelo')
  const [victoria, setVictoria] = useState(true)
  const [puntuacion, setPuntuacion] = useState(1500)
  const [duracion, setDuracion] = useState(300)
  const [mejoras, setMejoras] = useState('Aumento de daño')

  async function refrescar() {
    const r = await api.get<{ snapshots: Snapshot[] }>(`/historial/${usuario}`)
    setSnapshots(r.snapshots ?? [])
  }

  useEffect(() => {
    refrescar()
  }, [])

  async function guardar() {
    setLoading(true)
    try {
      const r = await api.post<{ log: string }>('/historial/guardar', {
        usuario,
        modo,
        victoria,
        puntuacion,
        duracionSegundos: duracion,
        mejorasAplicadas: mejoras
          .split(',')
          .map((m) => m.trim())
          .filter(Boolean),
      })
      setLog(r.log)
      await refrescar()
    } finally {
      setLoading(false)
    }
  }

  async function verDetalle(id: string) {
    const r = await api.get<{ snapshot: Snapshot; log: string }>(`/historial/${usuario}/${id}`)
    setSeleccion(r.snapshot)
    setLog(r.log)
  }

  async function limpiar() {
    setLoading(true)
    try {
      const r = await api.get<{ log: string }>(`/historial/${usuario}`)
      // usar DELETE manual
      await fetch(`/api/historial/${usuario}`, { method: 'DELETE' })
      setLog(r.log)
      await refrescar()
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Historial de partidas</h1>
        <PatternBadge
          patron="Memento"
          description="Cada partida finalizada se guarda como snapshot inmutable (Memento). El caretaker mantiene la lista por jugador y puede restaurar el estado al revisar el detalle."
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        <div className="panel p-5 lg:col-span-2 space-y-4">
          <h3 className="font-semibold flex items-center gap-2">
            <History size={16} /> Snapshots guardados ({snapshots.length})
          </h3>
          {snapshots.length === 0 && (
            <p className="text-slate-500 text-sm">No hay partidas en el historial.</p>
          )}
          <div className="space-y-2">
            {snapshots.map((s) => (
              <button
                key={s.id}
                onClick={() => verDetalle(s.id)}
                className="w-full panel p-4 text-left hover:border-violet-400/40 transition flex items-center gap-3"
              >
                <Trophy
                  size={18}
                  className={s.victoria ? 'text-emerald-400' : 'text-rose-400'}
                />
                <div className="flex-1">
                  <p className="font-semibold text-white">{s.modo}</p>
                  <p className="text-xs text-slate-400">{s.resumen}</p>
                </div>
                <div className="text-right">
                  <p className="font-display font-bold text-amber-300">{s.puntuacion}</p>
                  <p className="text-xs text-slate-500">{s.id}</p>
                </div>
              </button>
            ))}
          </div>
        </div>

        <div className="space-y-5">
          <div className="panel p-5 space-y-3">
            <h3 className="font-semibold flex items-center gap-2">
              <Save size={16} /> Guardar snapshot
            </h3>
            <select className="input" value={modo} onChange={(e) => setModo(e.target.value)}>
              {MODOS.map((m) => (
                <option key={m}>{m}</option>
              ))}
            </select>
            <div className="flex gap-2">
              <button
                type="button"
                onClick={() => setVictoria(true)}
                className={`flex-1 panel p-2 text-sm ${
                  victoria ? 'border-emerald-400/50 text-emerald-300' : 'text-slate-400'
                }`}
              >
                Victoria
              </button>
              <button
                type="button"
                onClick={() => setVictoria(false)}
                className={`flex-1 panel p-2 text-sm ${
                  !victoria ? 'border-rose-400/50 text-rose-300' : 'text-slate-400'
                }`}
              >
                Derrota
              </button>
            </div>
            <div>
              <label className="label">Puntuación: {puntuacion}</label>
              <input
                type="range"
                min={0}
                max={5000}
                value={puntuacion}
                onChange={(e) => setPuntuacion(Number(e.target.value))}
                className="w-full accent-violet-500"
              />
            </div>
            <div>
              <label className="label">Duración (segundos): {duracion}</label>
              <input
                type="range"
                min={60}
                max={1800}
                value={duracion}
                onChange={(e) => setDuracion(Number(e.target.value))}
                className="w-full accent-violet-500"
              />
            </div>
            <input
              className="input"
              placeholder="Mejoras separadas por coma"
              value={mejoras}
              onChange={(e) => setMejoras(e.target.value)}
            />
            <button className="btn-primary w-full justify-center" onClick={guardar} disabled={loading}>
              {loading ? <Loader2 className="animate-spin" size={14} /> : <Save size={14} />}
              Crear Memento
            </button>
            <button className="btn-ghost w-full justify-center" onClick={limpiar} disabled={loading}>
              <Trash2 size={14} />
              Limpiar historial
            </button>
          </div>
        </div>
      </div>

      {seleccion && (
        <div className="panel p-5 space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="font-semibold">Detalle restaurado — {seleccion.id}</h3>
            <button onClick={() => setSeleccion(null)} className="text-slate-400 hover:text-white">
              <X size={16} />
            </button>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 text-sm">
            <Stat label="Modo" value={seleccion.modo} />
            <Stat
              label="Resultado"
              value={seleccion.victoria ? 'Victoria' : 'Derrota'}
              accent={seleccion.victoria}
            />
            <Stat label="Puntuación" value={seleccion.puntuacion} />
            <Stat label="Duración" value={`${seleccion.duracionSegundos}s`} />
          </div>
          {seleccion.mejorasAplicadas.length > 0 && (
            <div>
              <p className="label">Mejoras</p>
              <div className="flex flex-wrap gap-2">
                {seleccion.mejorasAplicadas.map((m, i) => (
                  <span key={i} className="pattern-badge border-rose-400/40 bg-rose-400/10 text-rose-300">
                    {m}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>
      )}

      <Terminal output={log} />
    </div>
  )
}

function Stat({
  label,
  value,
  accent,
}: {
  label: string
  value: string | number
  accent?: boolean
}) {
  return (
    <div className="panel p-3">
      <p className="text-xs text-slate-500">{label}</p>
      <p
        className={`font-display font-bold mt-1 text-xl ${
          accent ? 'text-emerald-300' : 'text-white'
        }`}
      >
        {value}
      </p>
    </div>
  )
}
