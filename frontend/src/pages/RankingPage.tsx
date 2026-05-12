import { useEffect, useState } from 'react'
import { api } from '@/lib/api'
import { auth } from '@/lib/auth'
import { PatternBadge } from '@/components/PatternBadge'
import { Terminal } from '@/components/Terminal'
import { Crown, Medal, Trophy, BarChart3 } from 'lucide-react'

type Fila = {
  posicion: number
  usuario: string
  puntos: number
  elo: number
  victorias: number
  derrotas: number
  ratio: number
  valor: string
}

type Criterio = { clave: string; descripcion: string }

export function RankingPage() {
  const user = auth.get()
  const usuario = user?.usuario ?? 'oscar'
  const [criterios, setCriterios] = useState<Criterio[]>([])
  const [criterio, setCriterio] = useState('puntos')
  const [filas, setFilas] = useState<Fila[]>([])
  const [descripcion, setDescripcion] = useState('')
  const [log, setLog] = useState<string | undefined>()

  useEffect(() => {
    api
      .get<{ criterios: Criterio[] }>('/ranking/criterios')
      .then((r) => setCriterios(r.criterios ?? []))
  }, [])

  useEffect(() => {
    api
      .get<{ ranking: Fila[]; descripcion: string; log: string }>(`/ranking?criterio=${criterio}`)
      .then((r) => {
        setFilas(r.ranking ?? [])
        setDescripcion(r.descripcion ?? '')
        setLog(r.log)
      })
  }, [criterio])

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-2xl font-bold text-white mb-2">Ranking global</h1>
        <PatternBadge
          patron="Strategy"
          description="El servicio de ranking recibe una estrategia (puntos, ELO, victorias, ratio) y delega el ordenamiento. Cambiar de criterio no cambia el código del servicio."
        />
      </div>

      <div className="panel p-5 space-y-3">
        <p className="label">Estrategia activa</p>
        <div className="flex flex-wrap gap-2">
          {criterios.map((c) => (
            <button
              key={c.clave}
              onClick={() => setCriterio(c.clave)}
              className={`pattern-badge ${
                criterio === c.clave
                  ? 'border-sky-400/60 bg-sky-400/20 text-sky-200'
                  : 'border-white/10 bg-white/5 text-slate-300'
              }`}
            >
              <BarChart3 size={14} />
              {c.descripcion}
            </button>
          ))}
        </div>
      </div>

      <div className="panel overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-white/5 text-slate-400 uppercase text-xs tracking-wider">
            <tr>
              <th className="px-4 py-2 text-left">#</th>
              <th className="px-4 py-2 text-left">Jugador</th>
              <th className="px-4 py-2 text-right">{descripcion}</th>
              <th className="px-4 py-2 text-right">W/L</th>
            </tr>
          </thead>
          <tbody>
            {filas.map((f) => {
              const yo = f.usuario.toLowerCase() === usuario.toLowerCase()
              return (
                <tr
                  key={f.usuario}
                  className={`border-t border-white/5 ${yo ? 'bg-violet-500/10' : ''}`}
                >
                  <td className="px-4 py-3 font-display font-bold">
                    {f.posicion === 1 && <Crown size={16} className="inline text-amber-400 mr-1" />}
                    {f.posicion === 2 && <Medal size={16} className="inline text-slate-300 mr-1" />}
                    {f.posicion === 3 && <Medal size={16} className="inline text-orange-400 mr-1" />}
                    {f.posicion}
                  </td>
                  <td className="px-4 py-3">
                    <span className={yo ? 'text-violet-200 font-semibold' : 'text-slate-200'}>
                      {f.usuario}
                    </span>
                    {yo && <span className="text-xs text-violet-400 ml-2">(tú)</span>}
                  </td>
                  <td className="px-4 py-3 text-right font-mono text-amber-300">{f.valor}</td>
                  <td className="px-4 py-3 text-right font-mono text-slate-400">
                    {f.victorias}/{f.derrotas}
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>

      <div className="panel p-5">
        <h3 className="font-semibold flex items-center gap-2">
          <Trophy size={16} /> Tu posición ({usuario})
        </h3>
        <p className="text-sm text-slate-400 mt-2">
          Cambia la estrategia arriba para ver cómo varía tu posición sin que cambie el servicio
          de ranking.
        </p>
      </div>

      <Terminal output={log} />
    </div>
  )
}
