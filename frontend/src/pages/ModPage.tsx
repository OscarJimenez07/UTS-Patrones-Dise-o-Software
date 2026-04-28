import { Flag, Ban, MessageCircle, Clock } from 'lucide-react'

export function ModPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white">Panel de moderación</h1>
        <p className="text-slate-400 text-sm mt-1">
          Funciones de moderación (mock). El frontend ya enruta por rol, sustituyendo al
          Factory Method de Dashboard en consola.
        </p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card icon={<Flag />} title="Reportes pendientes" value="0" sub="Sin denuncias activas" />
        <Card icon={<Ban />} title="Sanciones activas" value="0" sub="Historial limpio" />
        <Card icon={<MessageCircle />} title="Mensajes en chat" value="—" sub="Modulo no expuesto" />
        <Card icon={<Clock />} title="Última revisión" value="hace 2 min" sub="Mock" />
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
      <div className="text-fuchsia-400">{icon}</div>
      <p className="text-xs text-slate-500 uppercase tracking-wider mt-3">{title}</p>
      <p className="font-display font-extrabold text-3xl text-white mt-1">{value}</p>
      <p className="text-xs text-slate-400 mt-1">{sub}</p>
    </div>
  )
}
