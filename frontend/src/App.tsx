import { Navigate, Route, Routes } from 'react-router-dom'
import { Layout } from '@/components/Layout'
import { LoginPage } from '@/pages/LoginPage'
import { DashboardPage } from '@/pages/DashboardPage'
import { TiendaPage } from '@/pages/TiendaPage'
import { PartidaPage } from '@/pages/PartidaPage'
import { NotificacionesPage } from '@/pages/NotificacionesPage'
import { MejorasPage } from '@/pages/MejorasPage'
import { AtaquesPage } from '@/pages/AtaquesPage'
import { ClanPage } from '@/pages/ClanPage'
import { HistorialPage } from '@/pages/HistorialPage'
import { LogrosPage } from '@/pages/LogrosPage'
import { RankingPage } from '@/pages/RankingPage'
import { ChatPage } from '@/pages/ChatPage'
import { MisionesPage } from '@/pages/MisionesPage'
import { AdminPage } from '@/pages/AdminPage'
import { ModPage } from '@/pages/ModPage'
import { auth } from '@/lib/auth'

function homeForRol() {
  const u = auth.get()
  if (!u) return '/login'
  if (u.rol === 'administrador') return '/admin'
  if (u.rol === 'moderador') return '/mod'
  return '/jugador'
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<Layout />}>
        <Route path="/jugador" element={<DashboardPage />} />
        <Route path="/tienda" element={<TiendaPage />} />
        <Route path="/partida" element={<PartidaPage />} />
        <Route path="/notificaciones" element={<NotificacionesPage />} />
        <Route path="/mejoras" element={<MejorasPage />} />
        <Route path="/ataques" element={<AtaquesPage />} />
        <Route path="/clan" element={<ClanPage />} />
        <Route path="/historial" element={<HistorialPage />} />
        <Route path="/logros" element={<LogrosPage />} />
        <Route path="/ranking" element={<RankingPage />} />
        <Route path="/chat" element={<ChatPage />} />
        <Route path="/misiones" element={<MisionesPage />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/mod" element={<ModPage />} />
      </Route>
      <Route path="/" element={<Navigate to={homeForRol()} replace />} />
      <Route path="*" element={<Navigate to={homeForRol()} replace />} />
    </Routes>
  )
}
