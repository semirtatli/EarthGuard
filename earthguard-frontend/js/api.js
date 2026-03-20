const API = {
    baseUrl: '/api',

    getToken() {
        return localStorage.getItem('jwt_token');
    },

    setToken(token) {
        localStorage.setItem('jwt_token', token);
    },

    clearToken() {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_info');
    },

    getUser() {
        const info = localStorage.getItem('user_info');
        return info ? JSON.parse(info) : null;
    },

    setUser(user) {
        localStorage.setItem('user_info', JSON.stringify(user));
    },

    isAuthenticated() {
        return !!this.getToken();
    },

    async request(path, options = {}) {
        const url = this.baseUrl + path;
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        const token = this.getToken();
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        const response = await fetch(url, {
            ...options,
            headers
        });

        if (response.status === 401) {
            this.clearToken();
            App.updateAuthUI();
        }

        if (!response.ok) {
            const error = await response.json().catch(() => ({ message: 'Request failed' }));
            throw error;
        }

        const text = await response.text();
        return text ? JSON.parse(text) : null;
    },

    async get(path) {
        return this.request(path);
    },

    async post(path, body) {
        return this.request(path, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    async put(path, body) {
        return this.request(path, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    }
};
