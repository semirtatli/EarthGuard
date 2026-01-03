package com.earthguard.notification.service;

import com.earthguard.notification.dto.EarthquakeEvent;

public interface NotificationService {
    void sendEmailNotification(EarthquakeEvent event);
    void sendWebSocketNotification(EarthquakeEvent event);
}