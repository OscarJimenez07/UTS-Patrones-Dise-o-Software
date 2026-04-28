import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Crown, Gem, Coins, Loader2, Sparkles, Clock } from 'lucide-react'

type Item = { nombre?: string; rareza?: string; descripcion?: string; monedas?: number; duracionMinutos?: number }
type Oferta = {
  nivel: string
  skin: Item
  potenciador: Item
  recompensa: Item
  log?: string
}

export function TiendaPage() {
  const [nivel, setNivel] = useState<'estandar' | 'premium'>('estandar')
  const [oferta, setOferta] = useState<Oferta | null>(null)
  const [loading, setLoading] = useState(false)

  async function cargar(n: 'estandar' | 'premium') {
    setLoading(true)
    setNivel(n)
    try {
      const r = await api.get<Oferta>(`/tienda/${n}`)
      setOferta(r)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    cargar('estandar')
  }, [])

  const isPremium = nivel === 'premium'

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Tienda virtual</h1>
        <PatternBadge
          patron="Abstract Factory"
          description="Las dos pestañas usan FÁBRICAS distintas (Estándar y Premium). Cada fábrica crea una familia completa: skin + potenciador + recompensa con calidades coherentes."
        />
      </div>

      <div className="grid grid-cols-2 gap-2 p-1 bg-ink-800/60 rounded-lg max-w-md">
        <button
          onClick={() => cargar('estandar')}
          className={`py-2 rounded-md text-sm font-semibold transition flex items-center justify-center gap-2 ${
            nivel === 'estandar' ? 'bg-emerald-600/30 text-white' : 'text-slate-400'
          }`}
        >
          <Coins size={14} /> Estándar
        </button>
        <button
          onClick={() => cargar('premium')}
          className={`py-2 rounded-md text-sm font-semibold transition flex items-center justify-center gap-2 ${
            nivel === 'premium' ? 'bg-amber-500/30 text-white' : 'text-slate-400'
          }`}
        >
          <Crown size={14} /> Premium
        </button>
      </div>

      {loading && (
        <div className="text-slate-400 flex items-center gap-2">
          <Loader2 className="animate-spin" size={16} /> Cargando...
        </div>
      )}

      {oferta && !loading && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card title="Skin" icon={<Sparkles className="text-violet-300" />}>
            <p className="text-lg font-semibold text-white">{oferta.skin.nombre}</p>
            <Rareza rareza={oferta.skin.rareza} premium={isPremium} />
          </Card>
          <Card title="Potenciador" icon={<Clock className="text-cyan-300" />}>
            <p className="text-lg font-semibold text-white">{oferta.potenciador.nombre}</p>
            <p className="text-sm text-slate-400 mt-1">
              Duración: {oferta.potenciador.duracionMinutos} min
            </p>
          </Card>
          <Card title="Recompensa" icon={<Gem className="text-amber-300" />}>
            <p className="text-sm text-slate-200">{oferta.recompensa.descripcion}</p>
            <p className="text-2xl font-display font-bold mt-2 bg-gradient-to-r from-amber-400 to-orange-500 bg-clip-text text-transparent">
              +{oferta.recompensa.monedas} monedas
            </p>
          </Card>
        </div>
      )}

      <Terminal output={oferta?.log} />
    </div>
  )
}

function Card({
  title,
  icon,
  children,
}: {
  title: string
  icon: React.ReactNode
  children: React.ReactNode
}) {
  return (
    <div className="panel p-5">
      <div className="flex items-center gap-2 text-xs uppercase tracking-wider text-slate-400">
        {icon} {title}
      </div>
      <div className="mt-3">{children}</div>
    </div>
  )
}

function Rareza({ rareza, premium }: { rareza?: string; premium: boolean }) {
  const cls = premium
    ? 'bg-gradient-to-r from-amber-400 to-orange-500 text-black'
    : 'bg-slate-700 text-slate-200'
  return (
    <span className={`inline-block mt-2 px-2 py-1 rounded text-xs font-bold ${cls}`}>
      {rareza}
    </span>
  )
}
