const Dashboard = {
    map: null,
    markers: [],
    refreshInterval: null,

    init() {
        this.initMap();
        this.loadEarthquakes();
        this.startAutoRefresh();

        document.getElementById('refresh-btn').addEventListener('click', () => this.loadEarthquakes());
        document.getElementById('limit-select').addEventListener('change', () => this.loadEarthquakes());
    },

    initMap() {
        this.map = L.map('map').setView([39.0, 35.0], 3);
        L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
            attribution: '&copy; OpenStreetMap &copy; CARTO',
            maxZoom: 18
        }).addTo(this.map);
    },

    startAutoRefresh() {
        this.refreshInterval = setInterval(() => this.loadEarthquakes(), 30000);
    },

    async loadEarthquakes() {
        const limit = document.getElementById('limit-select').value;
        const listEl = document.getElementById('earthquake-list');

        try {
            const earthquakes = await API.get('/earthquakes/recent?limit=' + limit);
            this.renderList(earthquakes, listEl);
            this.renderMap(earthquakes);
            this.updateStats(earthquakes);
        } catch (err) {
            listEl.innerHTML = '<div class="loading">Failed to load earthquakes</div>';
        }
    },

    getMagClass(mag) {
        if (mag >= 6) return 'mag-red';
        if (mag >= 4.5) return 'mag-orange';
        if (mag >= 2.5) return 'mag-yellow';
        return 'mag-green';
    },

    getMagColor(mag) {
        if (mag >= 6) return '#e0245e';
        if (mag >= 4.5) return '#f45d22';
        if (mag >= 2.5) return '#ffad1f';
        return '#17bf63';
    },

    formatTime(timestamp) {
        if (!timestamp) return '';
        const date = new Date(timestamp);
        const now = new Date();
        const diff = (now - date) / 1000;

        if (diff < 60) return 'just now';
        if (diff < 3600) return Math.floor(diff / 60) + 'm ago';
        if (diff < 86400) return Math.floor(diff / 3600) + 'h ago';
        return date.toLocaleDateString();
    },

    renderList(earthquakes, container) {
        if (!earthquakes || earthquakes.length === 0) {
            container.innerHTML = '<div class="loading">No earthquakes found</div>';
            return;
        }

        container.innerHTML = earthquakes.map(eq => `
            <div class="earthquake-item" data-lat="${eq.latitude}" data-lng="${eq.longitude}">
                <div class="mag-badge ${this.getMagClass(eq.magnitude)}">${eq.magnitude.toFixed(1)}</div>
                <div class="eq-info">
                    <div class="eq-location">${eq.location || 'Unknown'}</div>
                    <div class="eq-details">
                        Depth: ${eq.depth ? eq.depth.toFixed(1) + ' km' : 'N/A'} | ${this.formatTime(eq.timestamp)}
                    </div>
                </div>
            </div>
        `).join('');

        container.querySelectorAll('.earthquake-item').forEach(item => {
            item.addEventListener('click', () => {
                const lat = parseFloat(item.dataset.lat);
                const lng = parseFloat(item.dataset.lng);
                if (!isNaN(lat) && !isNaN(lng)) {
                    this.map.setView([lat, lng], 8);
                }
            });
        });
    },

    renderMap(earthquakes) {
        // Clear existing markers
        this.markers.forEach(m => this.map.removeLayer(m));
        this.markers = [];

        if (!earthquakes) return;

        earthquakes.forEach(eq => {
            if (eq.latitude == null || eq.longitude == null) return;

            const radius = Math.max(5, Math.min(25, eq.magnitude * 4));
            const color = this.getMagColor(eq.magnitude);

            const marker = L.circleMarker([eq.latitude, eq.longitude], {
                radius: radius,
                fillColor: color,
                color: color,
                weight: 1,
                opacity: 0.8,
                fillOpacity: 0.5
            }).addTo(this.map);

            marker.bindPopup(`
                <div class="popup-content">
                    <div class="popup-mag" style="color:${color}">M${eq.magnitude.toFixed(1)}</div>
                    <h3>${eq.location || 'Unknown'}</h3>
                    <p>Depth: ${eq.depth ? eq.depth.toFixed(1) + ' km' : 'N/A'}</p>
                    <p>${this.formatTime(eq.timestamp)}</p>
                    <p>${eq.latitude.toFixed(4)}, ${eq.longitude.toFixed(4)}</p>
                </div>
            `);

            this.markers.push(marker);
        });
    },

    updateStats(earthquakes) {
        document.getElementById('stat-total').textContent = earthquakes ? earthquakes.length : 0;
        const critical = earthquakes ? earthquakes.filter(eq => eq.magnitude >= 6).length : 0;
        document.getElementById('stat-critical').textContent = critical;
    },

    destroy() {
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
            this.refreshInterval = null;
        }
    }
};
