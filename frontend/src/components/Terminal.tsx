import { Terminal as TerminalIcon } from 'lucide-react'

export function Terminal({ output }: { output?: string }) {
  if (!output) return null
  return (
    <div className="space-y-2">
      <div className="flex items-center gap-2 text-xs uppercase tracking-wider text-slate-400">
        <TerminalIcon size={14} />
        Salida del backend (System.out capturado)
      </div>
      <pre className="terminal">{output}</pre>
    </div>
  )
}
