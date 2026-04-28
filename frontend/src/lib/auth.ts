const KEY = 'gameengine.user'

export type User = {
  usuario: string
  rol: 'jugador' | 'administrador' | 'moderador'
}

export const auth = {
  get(): User | null {
    const raw = localStorage.getItem(KEY)
    if (!raw) return null
    try {
      return JSON.parse(raw) as User
    } catch {
      return null
    }
  },
  set(u: User) {
    localStorage.setItem(KEY, JSON.stringify(u))
  },
  clear() {
    localStorage.removeItem(KEY)
  },
}
