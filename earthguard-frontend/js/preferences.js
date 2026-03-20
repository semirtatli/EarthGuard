const Preferences = {
    init() {
        document.getElementById('preferences-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.save();
        });
    },

    async load() {
        if (!API.isAuthenticated()) return;

        try {
            const prefs = await API.get('/preferences');
            if (prefs) {
                document.getElementById('pref-location').value = prefs.locationName || '';
                document.getElementById('pref-lat').value = prefs.preferredLatitude || '';
                document.getElementById('pref-lng').value = prefs.preferredLongitude || '';
                document.getElementById('pref-radius').value = prefs.radiusKm || 100;
                document.getElementById('pref-min-mag').value = prefs.minMagnitude || 4.0;
                document.getElementById('pref-email').checked = prefs.emailNotifications || false;
                document.getElementById('pref-push').checked = prefs.pushNotifications !== false;
                document.getElementById('pref-critical').checked = prefs.criticalOnly || false;
            }
        } catch (err) {
            // No existing preferences, use defaults
        }
    },

    async save() {
        const errorEl = document.getElementById('pref-error');
        const successEl = document.getElementById('pref-success');
        errorEl.textContent = '';
        successEl.textContent = '';

        const data = {
            locationName: document.getElementById('pref-location').value || null,
            preferredLatitude: parseFloat(document.getElementById('pref-lat').value) || null,
            preferredLongitude: parseFloat(document.getElementById('pref-lng').value) || null,
            radiusKm: parseFloat(document.getElementById('pref-radius').value) || null,
            minMagnitude: parseFloat(document.getElementById('pref-min-mag').value) || null,
            emailNotifications: document.getElementById('pref-email').checked,
            pushNotifications: document.getElementById('pref-push').checked,
            criticalOnly: document.getElementById('pref-critical').checked
        };

        try {
            await API.put('/preferences', data);
            successEl.textContent = 'Preferences saved successfully!';
            setTimeout(() => { successEl.textContent = ''; }, 3000);
        } catch (err) {
            errorEl.textContent = err.message || 'Failed to save preferences';
        }
    }
};
