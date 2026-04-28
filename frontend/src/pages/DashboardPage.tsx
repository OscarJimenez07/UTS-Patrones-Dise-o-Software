import { Link } from 'react-router-dom'
import { auth } from '@/lib/auth'
import {
  ShoppingBag,
  Gamepad2,
  Bell,
  Sparkles,
  Swords,
  Users,
  ArrowRight,
} from 'lucide-react'

const CARDS = [
  {
    to: '/tienda',
    title: 'Tienda',
    patron: 'Abstract Factory',
    desc: 'Familias de ítems Estándar y Premium creadas por fábricas distintas.',
    icon: ShoppingBag,
    grad: 'from-cyan-500/20 to-cyan-500/5',
    border: 'border-cyan-400/20',
  },
  {
    to: '/partida',
    title: 'Partidas',
    patron: 'Prototype + Facade',
    desc: 'Clona configuraciones e inicia partidas orquestando varios subsistemas.',
    icon: Gamepad2,
    grad: 'from-emerald-500/20 to-emerald-500/5',
    border: 'border-emerald-400/20',
  },
  {
    to: '/notificaciones',
    title: 'Notificaciones',
    patron: 'Adapter',
    desc: 'Mismo mensaje, dos canales (Discord, Correo) con interfaces incompatibles.',
    icon: Bell,
    grad: 'from-fuchsia-500/20 to-fuchsia-500/5',
    border: 'border-fuchsia-400/20',
  },
  {
    to: '/mejoras',
    title: 'Mejoras',
    patron: 'Decorator',
    desc: 'Apila efectos sobre una habilidad y ve el daño total recalculado.',
    icon: Sparkles,
    grad: 'from-rose-500/20 to-rose-500/5',
    border: 'border-rose-400/20',
  },
  {
    to: '/ataques',
    title: 'Ataques',
    patron: 'Bridge',
    desc: 'Combina 3 tipos de ataque × 3 elementos sin explosión de subclases.',
    icon: Swords,
    grad: 'from-sky-500/20 to-sky-500/5',
    border: 'border-sky-400/20',
  },
  {
    to: '/clan',
    title: 'Clan',
    patron: 'Composite',
    desc: 'Jerarquía de escuadrones y jugadores tratada de forma uniforme.',
    icon: Users,
    grad: 'from-lime-500/20 to-lime-500/5',
    border: 'border-lime-400/20',
  },
]

export function DashboardPage() {
  const user = auth.get()
  return (
    <div className="space-y-8">
      <header>
        <p className="text-sm text-slate-400">Bienvenido,</p>
        <h1 className="font-display text-3xl font-extrabold tracking-wider bg-gradient-to-r from-violet-400 via-fuchsia-400 to-cyan-300 bg-clip-text text-transparent">
          {user?.usuario}
        </h1>
        <p className="text-slate-400 mt-2">
          Cada módulo demuestra un patrón de diseño en vivo. Al usar la
          funcionalidad verás la salida real del backend Java.
        </p>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {CARDS.map((c) => (
          <Link
            key={c.to}
            to={c.to}
            className={`panel p-6 hover:scale-[1.02] hover:border-white/20 transition group bg-gradient-to-br ${c.grad} ${c.border}`}
          >
            <div className="flex items-start justify-between">
              <c.icon className="text-white" size={28} />
              <ArrowRight
                size={18}
                className="text-slate-400 group-hover:text-white group-hover:translate-x-1 transition"
              />
            </div>
            <h3 className="font-display text-xl font-bold mt-4 text-white">
              {c.title}
            </h3>
            <p className="text-xs text-slate-400 uppercase tracking-wider mt-1">
              {c.patron}
            </p>
            <p className="text-sm text-slate-300 mt-3">{c.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  )
}
