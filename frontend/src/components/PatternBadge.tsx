import { cn } from '@/lib/cn'
import { Sparkles } from 'lucide-react'

const PATTERN_STYLES: Record<string, string> = {
  Singleton: 'border-amber-400/40 bg-amber-400/10 text-amber-300',
  'Abstract Factory': 'border-cyan-400/40 bg-cyan-400/10 text-cyan-300',
  Prototype: 'border-emerald-400/40 bg-emerald-400/10 text-emerald-300',
  Facade: 'border-violet-400/40 bg-violet-400/10 text-violet-300',
  Adapter: 'border-fuchsia-400/40 bg-fuchsia-400/10 text-fuchsia-300',
  Decorator: 'border-rose-400/40 bg-rose-400/10 text-rose-300',
  Bridge: 'border-sky-400/40 bg-sky-400/10 text-sky-300',
  Composite: 'border-lime-400/40 bg-lime-400/10 text-lime-300',
}

export function PatternBadge({
  patron,
  description,
}: {
  patron: string
  description?: string
}) {
  const cls = PATTERN_STYLES[patron] ?? 'border-white/20 bg-white/5 text-slate-200'
  return (
    <div className="flex items-start gap-3 flex-wrap">
      <span className={cn('pattern-badge', cls)}>
        <Sparkles size={14} />
        Patrón: {patron}
      </span>
      {description && (
        <p className="text-sm text-slate-400 max-w-3xl">{description}</p>
      )}
    </div>
  )
}
