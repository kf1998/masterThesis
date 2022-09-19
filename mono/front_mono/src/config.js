export default {
    serverBaseUrl: 'http://' + (import.meta.env.VITE_SERVER_URL || 'localhost:8080') + '/api'
}