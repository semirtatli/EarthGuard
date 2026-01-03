package com.earthguard.notification.service.email;

import com.earthguard.notification.dto.EarthquakeEvent;

public interface EmailService {
    void sendEarthquakeAlert(EarthquakeEvent event, String recipient);
    void sendBulkAlert(EarthquakeEvent event, String[] recipients);
}