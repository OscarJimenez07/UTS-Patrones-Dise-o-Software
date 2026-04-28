import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom'
import { auth } from '@/lib/auth'
import {
  Gamepad2,
  Home,
  LogOut,
  ShoppingBag,
  Bell,
  Sparkles,
  Swords,
  Users,
  Shield,
  Wrench,
} from 'lucide-react'
import { cn } from '@/lib/cn'

type NavItem = {
  to: string
  label: string
  icon: React.ComponentType<{ size?: number }>
  roles: Array<'jugador' | 'administrador' | 'moderador'>
}

const NAV: NavItem[] = [
  { to: '/jugador', label: 'Inicio', icon: Home, roles: ['jugador'] },
  { to: '/tienda', label: 'Tienda', icon: ShoppingBag, roles: ['jugador'] },
  { to: '/partida', label: 'Partidas', icon: Gamepad2, roles: ['jugador'] },
  { to: '/notificaciones', label: 'Notificaciones', icon: Bell, roles: ['jugador'] },
  { to: '/mejoras', label: 'Mejoras', icon: Sparkles, roles: ['jugador'] },
  { to: '/ataques', label: 'Ataques', icon: Swords, roles: ['jugador'] },
  { to: '/clan', label: 'Clan', icon: Users, roles: ['jugador'] },
  { to: '/admin', label: 'Panel admin', icon: Shield, roles: ['administrador'] },
  { to: '/mod', label: 'Panel mod', icon: Wrench, roles: ['moderador'] },
]

export function Layout() {
  const user = auth.get()
  const navigate = useNavigate()

  if (!user) {
    navigate('/login', { replace: true })
    return null
  }

  const items = NAV.filter((n) => n.roles.includes(user.rol))

  return (
    <div className="min-h-full flex">
      <aside className="w-64 shrink-0 border-r border-white/5 bg-ink-900/60 backdrop-blur p-5 flex flex-col">
        <Link
          to="/"
          className="font-display font-extrabold text-xl tracking-wider bg-gradient-to-r from-violet-400 via-fuchsia-400 to-cyan-300 bg-clip-text text-transparent"
        >
          GAMEENGINE
        </Link>
        <p className="text-xs text-slate-500 mt-1 mb-8">Patrones · en vivo</p>

        <nav className="flex-1 space-y-1">
          {items.map((it) => (
            <NavLink
              key={it.to}
              to={it.to}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 px-3 py-2 rounded-lg text-sm transition',
                  isActive
                    ? 'bg-gradient-to-r from-violet-600/30 to-fuchsia-600/20 text-white border border-violet-400/30'
                    : 'text-slate-400 hover:bg-white/5 hover:text-slate-100',
                )
              }
            >
              <it.icon size={16} />
              {it.label}
            </NavLink>
          ))}
        </nav>

        <div className="mt-8 pt-4 border-t border-white/5">
          <p className="text-xs text-slate-500">Sesión activa</p>
          <p className="text-sm font-semibold text-slate-100">{user.usuario}</p>
          <p className="text-xs text-slate-400 capitalize mb-3">{user.rol}</p>
          <button
            onClick={() => {
              auth.clear()
              navigate('/login', { replace: true })
            }}
            className="btn-ghost w-full justify-center text-xs"
          >
            <LogOut size={14} />
            Cerrar sesión
          </button>
        </div>
      </aside>

      <main className="flex-1 p-8 overflow-auto">
        <Outlet />
      </main>
    </div>
  )
}
