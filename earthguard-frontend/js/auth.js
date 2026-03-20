const Auth = {
    init() {
        document.getElementById('login-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.login();
        });

        document.getElementById('register-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.register();
        });

        document.querySelectorAll('.auth-tab').forEach(tab => {
            tab.addEventListener('click', () => this.switchTab(tab.dataset.tab));
        });

        document.getElementById('logout-btn').addEventListener('click', () => this.logout());
    },

    switchTab(tab) {
        document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
        document.querySelector(`.auth-tab[data-tab="${tab}"]`).classList.add('active');

        document.getElementById('login-form').style.display = tab === 'login' ? 'block' : 'none';
        document.getElementById('register-form').style.display = tab === 'register' ? 'block' : 'none';
    },

    async login() {
        const errorEl = document.getElementById('login-error');
        errorEl.textContent = '';

        const username = document.getElementById('login-username').value;
        const password = document.getElementById('login-password').value;

        try {
            const data = await API.post('/auth/login', { username, password });
            API.setToken(data.token);
            API.setUser({ username: data.username, email: data.email, role: data.role });
            App.updateAuthUI();
            App.navigate('dashboard');
            EarthguardWS.connect();
        } catch (err) {
            errorEl.textContent = err.message || 'Login failed';
        }
    },

    async register() {
        const errorEl = document.getElementById('register-error');
        errorEl.textContent = '';

        const fullName = document.getElementById('reg-fullname').value;
        const username = document.getElementById('reg-username').value;
        const email = document.getElementById('reg-email').value;
        const password = document.getElementById('reg-password').value;

        try {
            const data = await API.post('/auth/register', { fullName, username, email, password });
            API.setToken(data.token);
            API.setUser({ username: data.username, email: data.email, role: data.role });
            App.updateAuthUI();
            App.navigate('dashboard');
            EarthguardWS.connect();
        } catch (err) {
            errorEl.textContent = err.message || 'Registration failed';
        }
    },

    logout() {
        API.clearToken();
        EarthguardWS.disconnect();
        App.updateAuthUI();
        App.navigate('dashboard');
    }
};
