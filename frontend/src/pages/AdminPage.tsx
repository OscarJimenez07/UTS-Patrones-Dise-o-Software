import { Server, Users, BarChart3, Activity } from 'lucide-react'

export function AdminPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white">Panel de administrador</h1>
        <p className="text-slate-400 text-sm mt-1">
          Visualización de sistema. Este rol no consume patrones del lado cliente: la
          ruta misma demuestra que el frontend reemplaza al Factory Method de Dashboard.
        </p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card icon={<Users />} title="Usuarios registrados" value="2" sub="Datos sembrados en Singleton" />
        <Card icon={<Server />} title="Servidores activos" value="3" sub="Mock — modulo en desarrollo" />
        <Card icon={<Activity />} title="Jugadores conectados" value="127" sub="Mock" />
        <Card icon={<BarChart3 />} title="Partidas hoy" value="45" sub="Mock" />
      </div>
    </div>
  )
}

function Card({
  icon,
  title,
  value,
  sub,
}: {
  icon: React.ReactNode
  title: string
  value: string
  sub: string
}) {
  return (
    <div className="panel p-5">
      <div className="text-violet-400">{icon}</div>
      <p className="text-xs text-slate-500 uppercase tracking-wider mt-3">{title}</p>
      <p className="font-display font-extrabold text-3xl text-white mt-1">{value}</p>
      <p className="text-xs text-slate-400 mt-1">{sub}</p>
    </div>
  )
}
