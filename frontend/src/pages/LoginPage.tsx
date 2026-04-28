import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '@/lib/api'
import { auth, type User } from '@/lib/auth'
import { Gamepad2, KeyRound, UserPlus, LogIn, Loader2 } from 'lucide-react'

export function LoginPage() {
  const [tab, setTab] = useState<'login' | 'registro'>('login')
  const [usuario, setUsuario] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [rol, setRol] = useState<'jugador' | 'administrador' | 'moderador'>('jugador')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  async function handleLogin() {
    setError(null)
    setLoading(true)
    try {
      const r = await api.post<{ ok: boolean; usuario: string; rol: User['rol'] }>(
        '/auth/login',
        { usuario, contrasena },
      )
      if (!r.ok) {
        setError('Credenciales incorrectas')
        return
      }
      auth.set({ usuario: r.usuario, rol: r.rol })
      const dest =
        r.rol === 'administrador' ? '/admin' : r.rol === 'moderador' ? '/mod' : '/jugador'
      navigate(dest, { replace: true })
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : 'Error de red')
    } finally {
      setLoading(false)
    }
  }

  async function handleRegistro() {
    setError(null)
    setLoading(true)
    try {
      const r = await api.post<{ ok: boolean; usuario: string; rol: User['rol'] }>(
        '/auth/registro',
        { usuario, contrasena, rol },
      )
      if (!r.ok) {
        setError('No se pudo registrar')
        return
      }
      auth.set({ usuario: r.usuario, rol: r.rol })
      const dest =
        r.rol === 'administrador' ? '/admin' : r.rol === 'moderador' ? '/mod' : '/jugador'
      navigate(dest, { replace: true })
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : 'Error de red')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-full flex items-center justify-center p-6">
      <div className="w-full max-w-md panel p-8 space-y-6">
        <div className="text-center space-y-2">
          <div className="inline-flex items-center gap-3 font-display font-extrabold text-3xl tracking-wider bg-gradient-to-r from-violet-400 via-fuchsia-400 to-cyan-300 bg-clip-text text-transparent">
            <Gamepad2 className="text-violet-400" size={32} />
            GAMEENGINE
          </div>
          <p className="text-sm text-slate-400">
            Motor de Juegos Multijugador · 8 patrones de diseño en vivo
          </p>
        </div>

        <div className="grid grid-cols-2 gap-2 p-1 bg-ink-800/60 rounded-lg">
          <button
            onClick={() => setTab('login')}
            className={`py-2 rounded-md text-sm font-semibold transition ${
              tab === 'login' ? 'bg-violet-600/30 text-white' : 'text-slate-400'
            }`}
          >
            Iniciar sesión
          </button>
          <button
            onClick={() => setTab('registro')}
            className={`py-2 rounded-md text-sm font-semibold transition ${
              tab === 'registro' ? 'bg-violet-600/30 text-white' : 'text-slate-400'
            }`}
          >
            Registrarse
          </button>
        </div>

        <div className="space-y-3">
          <div>
            <label className="label">Usuario</label>
            <input
              className="input"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              placeholder="Tu usuario"
            />
          </div>
          <div>
            <label className="label">Contraseña</label>
            <input
              className="input"
              type="password"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              placeholder="••••"
            />
          </div>
          {tab === 'registro' && (
            <div>
              <label className="label">Rol</label>
              <select
                className="input"
                value={rol}
                onChange={(e) => setRol(e.target.value as User['rol'])}
              >
                <option value="jugador">Jugador</option>
                <option value="moderador">Moderador</option>
                <option value="administrador">Administrador</option>
              </select>
            </div>
          )}
        </div>

        {error && (
          <div className="text-sm text-rose-300 bg-rose-500/10 border border-rose-500/30 rounded-lg p-3">
            {error}
          </div>
        )}

        <button
          className="btn-primary w-full justify-center"
          onClick={tab === 'login' ? handleLogin : handleRegistro}
          disabled={loading || !usuario || !contrasena}
        >
          {loading ? (
            <Loader2 className="animate-spin" size={16} />
          ) : tab === 'login' ? (
            <LogIn size={16} />
          ) : (
            <UserPlus size={16} />
          )}
          {tab === 'login' ? 'Entrar' : 'Crear cuenta'}
        </button>

        <div className="text-xs text-slate-500 text-center">
          <div className="flex items-center gap-2 justify-center">
            <KeyRound size={12} /> Patrón demostrado: Singleton (sesión única)
          </div>
        </div>
      </div>
    </div>
  )
}
