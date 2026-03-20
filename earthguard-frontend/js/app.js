const App = {
    currentPage: 'dashboard',

    init() {
        Auth.init();
        Preferences.init();

        // Navigation
        document.querySelectorAll('.nav-link[data-page]').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                this.navigate(link.dataset.page);
            });
        });

        this.updateAuthUI();
        this.navigate('dashboard');

        // Auto-connect WebSocket if authenticated
        if (API.isAuthenticated()) {
            EarthguardWS.connect();
        }
    },

    navigate(page) {
        // Auth guard for preferences
        if (page === 'preferences' && !API.isAuthenticated()) {
            page = 'login';
        }

        this.currentPage = page;

        // Update pages
        document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
        const pageEl = document.getElementById('page-' + page);
        if (pageEl) pageEl.classList.add('active');

        // Update nav
        document.querySelectorAll('.nav-link[data-page]').forEach(l => l.classList.remove('active'));
        const activeLink = document.querySelector(`.nav-link[data-page="${page}"]`);
        if (activeLink) activeLink.classList.add('active');

        // Page-specific init
        if (page === 'dashboard') {
            // Re-initialize map if needed (Leaflet needs visible container)
            setTimeout(() => {
                if (Dashboard.map) {
                    Dashboard.map.invalidateSize();
                } else {
                    Dashboard.init();
                }
            }, 100);
        } else if (page === 'preferences') {
            Preferences.load();
        }
    },

    updateAuthUI() {
        const isAuth = API.isAuthenticated();
        const user = API.getUser();

        document.getElementById('login-link').style.display = isAuth ? 'none' : '';
        document.getElementById('user-info').style.display = isAuth ? 'flex' : 'none';

        document.querySelectorAll('.auth-only').forEach(el => {
            el.style.display = isAuth ? '' : 'none';
        });

        if (isAuth && user) {
            document.getElementById('username-display').textContent = user.username;
        }
    },

    showToast(title, body, type = 'info', duration = 5000) {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = 'toast toast-' + type;
        toast.innerHTML = `
            <div class="toast-title">${title}</div>
            <div class="toast-body">${body}</div>
        `;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transition = 'opacity 0.3s';
            setTimeout(() => toast.remove(), 300);
        }, duration);
    }
};

document.addEventListener('DOMContentLoaded', () => App.init());
