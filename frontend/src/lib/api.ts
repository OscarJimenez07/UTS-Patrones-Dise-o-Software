const BASE = '/api'

export type ApiResponse<T = Record<string, unknown>> = T & {
  ok?: boolean
  log?: string
  patron?: string
  error?: string
}

async function request<T>(
  method: string,
  path: string,
  body?: unknown,
): Promise<ApiResponse<T>> {
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: body ? { 'Content-Type': 'application/json' } : undefined,
    body: body ? JSON.stringify(body) : undefined,
  })
  if (!res.ok) {
    const text = await res.text()
    throw new Error(`HTTP ${res.status}: ${text || res.statusText}`)
  }
  return res.json()
}

export const api = {
  get: <T = Record<string, unknown>>(path: string) => request<T>('GET', path),
  post: <T = Record<string, unknown>>(path: string, body?: unknown) =>
    request<T>('POST', path, body),
}
