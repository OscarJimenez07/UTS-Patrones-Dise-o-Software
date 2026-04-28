import { useState } from 'react'
import { api } from '@/lib/api'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Flame, Skull, Crosshair, Heart, Zap, X, Loader2 } from 'lucide-react'

const BASES: { key: string; label: string; dano: number }[] = [
  { key: 'golpe', label: 'Golpe Físico', dano: 25 },
  { key: 'disparo', label: 'Disparo Mágico', dano: 40 },
  { key: 'hechizo', label: 'Hechizo Curativo', dano: 10 },
]

const MEJORAS: { key: string; label: string; dano: number; icon: React.ComponentType<{ size?: number }>; color: string }[] = [
  { key: 'fuego', label: 'Fuego', dano: 15, icon: Flame, color: 'text-orange-400' },
  { key: 'veneno', label: 'Veneno', dano: 10, icon: Skull, color: 'text-lime-400' },
  { key: 'area', label: 'Área', dano: 8, icon: Crosshair, color: 'text-cyan-400' },
  { key: 'robo', label: 'Robo de Vida', dano: 5, icon: Heart, color: 'text-rose-400' },
]

export function MejorasPage() {
  const [base, setBase] = useState('disparo')
  const [stack, setStack] = useState<string[]>([])
  const [resultado, setResultado] = useState<{ descripcion?: string; danoTotal?: number; log?: string } | null>(null)
  const [loading, setLoading] = useState(false)

  const baseDano = BASES.find((b) => b.key === base)?.dano ?? 0
  const stackDano = stack.reduce((acc, k) => acc + (MEJORAS.find((m) => m.key === k)?.dano ?? 0), 0)
  const total = baseDano + stackDano

  function add(k: string) {
    setStack((s) => [...s, k])
  }
  function removeAt(i: number) {
    setStack((s) => s.filter((_, idx) => idx !== i))
  }

  async function ejecutar() {
    setLoading(true)
    try {
      const r = await api.post<{ descripcion: string; danoTotal: number; log: string }>(
        '/mejoras/aplicar',
        { base, mejoras: stack },
      )
      setResultado(r)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Mejoras de habilidades</h1>
        <PatternBadge
          patron="Decorator"
          description="Cada mejora ENVUELVE la habilidad anterior. El orden importa: cambia el orden y verás cómo cambia la descripción acumulada."
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <div className="panel p-5 space-y-4">
          <div>
            <p className="label">Habilidad base</p>
            <div className="grid grid-cols-3 gap-2">
              {BASES.map((b) => (
                <button
                  key={b.key}
                  onClick={() => setBase(b.key)}
                  className={`p-3 rounded-lg border text-sm transition ${
                    base === b.key
                      ? 'bg-violet-600/30 border-violet-400/40 text-white'
                      : 'bg-ink-800/60 border-white/5 text-slate-400'
                  }`}
                >
                  <p className="font-semibold">{b.label}</p>
                  <p className="text-xs opacity-70">{b.dano} dmg</p>
                </button>
              ))}
            </div>
          </div>

          <div>
            <p className="label">Mejoras disponibles (clic para apilar)</p>
            <div className="grid grid-cols-2 gap-2">
              {MEJORAS.map((m) => (
                <button
                  key={m.key}
                  onClick={() => add(m.key)}
                  className="p-3 rounded-lg border border-white/5 bg-ink-800/60 hover:bg-ink-800 hover:border-white/15 transition flex items-center gap-2 text-sm"
                >
                  <m.icon size={16} />
                  <span className={`font-semibold ${m.color}`}>{m.label}</span>
                  <span className="ml-auto text-xs text-slate-500">+{m.dano}</span>
                </button>
              ))}
            </div>
          </div>
        </div>

        <div className="panel p-5 space-y-4">
          <div>
            <p className="label">Pila de mejoras (orden = orden de envoltura)</p>
            <div className="flex flex-wrap gap-2 min-h-[60px] p-3 bg-ink-800/40 rounded-lg border border-dashed border-white/10">
              {stack.length === 0 ? (
                <p className="text-sm text-slate-500">Sin mejoras todavía</p>
              ) : (
                stack.map((k, i) => {
                  const m = MEJORAS.find((x) => x.key === k)!
                  return (
                    <span
                      key={i}
                      className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-violet-600/20 border border-violet-400/30 text-sm"
                    >
                      <m.icon size={12} />
                      {m.label}
                      <button onClick={() => removeAt(i)} className="hover:text-rose-300">
                        <X size={12} />
                      </button>
                    </span>
                  )
                })
              )}
            </div>
          </div>

          <div className="bg-gradient-to-r from-violet-600/20 to-fuchsia-600/20 rounded-xl p-5 border border-violet-400/30">
            <p className="text-xs text-slate-400 uppercase tracking-wider">Daño total acumulado</p>
            <p className="font-display font-extrabold text-5xl bg-gradient-to-r from-amber-300 to-rose-400 bg-clip-text text-transparent">
              {total}
            </p>
          </div>

          <button className="btn-primary w-full justify-center" onClick={ejecutar} disabled={loading}>
            {loading ? <Loader2 className="animate-spin" size={14} /> : <Zap size={14} />}
            Ejecutar habilidad
          </button>

          {resultado && (
            <div className="text-sm text-slate-300">
              <p className="text-slate-500">Descripción:</p>
              <p className="font-mono text-emerald-300">{resultado.descripcion}</p>
              <p className="text-slate-500 mt-2">Backend reportó:</p>
              <p className="font-mono text-amber-300">{resultado.danoTotal} de daño</p>
            </div>
          )}
        </div>
      </div>

      <Terminal output={resultado?.log} />
    </div>
  )
}
