import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Sword, Crosshair, Wand2, Flame, Snowflake, Zap, Loader2 } from 'lucide-react'

const TIPOS = [
  { key: 'cuerpo', label: 'Cuerpo a Cuerpo', icon: Sword, dano: 30 },
  { key: 'distancia', label: 'A Distancia', icon: Crosshair, dano: 22 },
  { key: 'magico', label: 'Mágico', icon: Wand2, dano: 35 },
] as const

const ELEMENTOS = [
  { key: 'fuego', label: 'Fuego', icon: Flame, color: 'text-orange-400', dano: 20 },
  { key: 'hielo', label: 'Hielo', icon: Snowflake, color: 'text-cyan-300', dano: 12 },
  { key: 'electrico', label: 'Eléctrico', icon: Zap, color: 'text-amber-300', dano: 18 },
] as const

type Combo = {
  tipoClave: string
  elementoClave: string
  tipo: string
  elemento: string
  danoTotal: number
  efecto: string
}

export function AtaquesPage() {
  const [combos, setCombos] = useState<Combo[]>([])
  const [tipo, setTipo] = useState<string>('magico')
  const [elemento, setElemento] = useState<string>('hielo')
  const [resultado, setResultado] = useState<{
    descripcion?: string
    danoBase?: number
    danoElemento?: number
    danoTotal?: number
    efecto?: string
    log?: string
  } | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    api
      .get<{ combinaciones: Combo[] }>('/ataques/combinaciones')
      .then((r) => setCombos(r.combinaciones ?? []))
  }, [])

  async function ejecutar() {
    setLoading(true)
    try {
      const r = await api.post<{ descripcion: string; danoBase: number; danoElemento: number; danoTotal: number; efecto: string; log: string }>(
        '/ataques/ejecutar',
        { tipo, elemento },
      )
      setResultado(r)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Sistema de ataques</h1>
        <PatternBadge
          patron="Bridge"
          description="3 tipos de ataque × 3 elementos = 9 combinaciones, sin necesidad de 9 clases. Tipo y elemento varían independientemente."
        />
      </div>

      <div className="panel p-5 space-y-5">
        <p className="label">Matriz 9 combinaciones (clic para seleccionar)</p>
        <div className="grid grid-cols-3 gap-2">
          {TIPOS.map((t) => (
            <div key={t.key} className="text-center text-xs uppercase tracking-wider text-slate-400">
              {t.label}
            </div>
          ))}
          {ELEMENTOS.flatMap((e) =>
            TIPOS.map((t) => {
              const c = combos.find(
                (x) => x.tipoClave === t.key && x.elementoClave === e.key,
              )
              const selected = tipo === t.key && elemento === e.key
              return (
                <button
                  key={`${t.key}-${e.key}`}
                  onClick={() => {
                    setTipo(t.key)
                    setElemento(e.key)
                  }}
                  className={`panel p-4 text-left transition ${
                    selected ? 'border-sky-400/60 shadow-neon-cyan' : ''
                  }`}
                >
                  <div className="flex items-center gap-2">
                    <t.icon size={16} className="text-slate-300" />
                    <e.icon size={16} className={e.color} />
                  </div>
                  <p className="text-sm font-semibold mt-2">
                    {t.label} de {e.label}
                  </p>
                  <p className="text-2xl font-display font-bold text-white mt-1">
                    {c?.danoTotal ?? '...'}
                  </p>
                </button>
              )
            }),
          )}
        </div>

        <button className="btn-primary" onClick={ejecutar} disabled={loading}>
          {loading ? <Loader2 className="animate-spin" size={14} /> : <Zap size={14} />}
          Ejecutar ataque seleccionado
        </button>

        {resultado && (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 text-sm">
            <Stat label="Daño base" value={resultado.danoBase} />
            <Stat label="Daño elemento" value={`+${resultado.danoElemento}`} />
            <Stat label="Daño total" value={resultado.danoTotal} accent />
            <div className="panel p-3">
              <p className="text-xs text-slate-500">Efecto</p>
              <p className="text-xs text-amber-300 mt-1">{resultado.efecto}</p>
            </div>
          </div>
        )}
      </div>

      <Terminal output={resultado?.log} />
    </div>
  )
}

function Stat({ label, value, accent }: { label: string; value: string | number | undefined; accent?: boolean }) {
  return (
    <div className="panel p-3">
      <p className="text-xs text-slate-500">{label}</p>
      <p
        className={`font-display font-bold mt-1 ${
          accent
            ? 'text-3xl bg-gradient-to-r from-amber-300 to-rose-400 bg-clip-text text-transparent'
            : 'text-2xl text-white'
        }`}
      >
        {value ?? '-'}
      </p>
    </div>
  )
}
