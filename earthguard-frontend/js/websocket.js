const EarthguardWS = {
    stompClient: null,
    connected: false,

    connect() {
        if (this.connected) return;

        try {
            const socket = new SockJS('/ws');
            this.stompClient = Stomp.over(socket);
            this.stompClient.debug = null; // disable debug logs

            this.stompClient.connect({}, () => {
                this.connected = true;
                console.log('WebSocket connected');

                this.stompClient.subscribe('/topic/earthquakes', (message) => {
                    const event = JSON.parse(message.body);
                    this.onEarthquakeEvent(event);
                });

                this.stompClient.subscribe('/topic/critical-alerts', (message) => {
                    const alert = JSON.parse(message.body);
                    this.onAlert(alert);
                });
            }, (error) => {
                console.warn('WebSocket connection failed:', error);
                this.connected = false;
                setTimeout(() => this.connect(), 5000);
            });
        } catch (e) {
            console.warn('WebSocket setup error:', e);
        }
    },

    disconnect() {
        if (this.stompClient && this.connected) {
            this.stompClient.disconnect();
            this.connected = false;
        }
    },

    onEarthquakeEvent(event) {
        const eq = event.earthquake || event;
        const mag = eq.magnitude || 0;

        let toastType = 'info';
        if (mag >= 6) toastType = 'danger';
        else if (mag >= 4.5) toastType = 'warning';

        App.showToast(
            `M${mag.toFixed(1)} Earthquake`,
            eq.location || 'Unknown location',
            toastType
        );

        // Refresh dashboard
        if (typeof Dashboard !== 'undefined') {
            Dashboard.loadEarthquakes();
        }
    },

    onAlert(alert) {
        App.showToast(
            'Alert: ' + (alert.title || 'Earthquake Alert'),
            alert.message || '',
            'danger',
            8000
        );
    }
};
