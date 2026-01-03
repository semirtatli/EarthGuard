package com.earthguard.notification.service.email;

import com.earthguard.notification.dto.EarthquakeEvent;

public class EmailTemplate {

    public static String getSubject(EarthquakeEvent event) {
        if (event.getMagnitude() >= 7.0) {
            return String.format("🚨 CRITICAL EARTHQUAKE ALERT - Magnitude %.1f", event.getMagnitude());
        } else if (event.getMagnitude() >= 6.0) {
            return String.format("⚠️ HIGH ALERT - Earthquake Magnitude %.1f", event.getMagnitude());
        } else if (event.getMagnitude() >= 5.0) {
            return String.format("📢 Earthquake Alert - Magnitude %.1f", event.getMagnitude());
        }
        return String.format("ℹ️ Earthquake Detected - Magnitude %.1f", event.getMagnitude());
    }

    public static String getHtmlBody(EarthquakeEvent event) {
        String alertColor = getAlertColor(event.getMagnitude());
        String alertEmoji = getAlertEmoji(event.getMagnitude());

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: %s; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; margin-top: 20px; border-radius: 5px; }
                    .detail-row { padding: 10px 0; border-bottom: 1px solid #ddd; }
                    .label { font-weight: bold; color: #555; }
                    .value { color: #333; }
                    .footer { margin-top: 20px; padding-top: 20px; border-top: 2px solid #ddd; font-size: 12px; color: #777; text-align: center; }
                    .alert-badge { display: inline-block; padding: 5px 10px; background-color: %s; color: white; border-radius: 3px; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>%s Earthquake Alert</h1>
                        <p style="margin: 0; font-size: 18px;">EarthGuard Early Warning System</p>
                    </div>
                    
                    <div class="content">
                        <h2>Earthquake Details</h2>
                        
                        <div class="detail-row">
                            <span class="label">Event ID:</span>
                            <span class="value">%s</span>
                        </div>
                        
                        <div class="detail-row">
                            <span class="label">Magnitude:</span>
                            <span class="value">%.1f</span>
                            <span class="alert-badge">%s</span>
                        </div>
                        
                        <div class="detail-row">
                            <span class="label">Location:</span>
                            <span class="value">%s</span>
                        </div>
                        
                        <div class="detail-row">
                            <span class="label">Coordinates:</span>
                            <span class="value">%.4f°, %.4f°</span>
                        </div>
                        
                        <div class="detail-row">
                            <span class="label">Time:</span>
                            <span class="value">%s</span>
                        </div>
                        
                        <div class="detail-row">
                            <span class="label">Alert Level:</span>
                            <span class="value">%s</span>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p><strong>EarthGuard</strong> - Earthquake Early Warning System</p>
                        <p>This is an automated alert. For more information, visit our dashboard.</p>
                        <p style="font-size: 10px; margin-top: 10px;">Event processed at: %s</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                alertColor,
                alertColor,
                alertEmoji,
                event.getEarthquakeId(),
                event.getMagnitude(),
                event.getAlertLevel(),
                event.getLocation(),
                event.getLatitude(),
                event.getLongitude(),
                event.getTimestamp(),
                event.getAlertLevel(),
                event.getEventTimestamp()
        );
    }

    private static String getAlertColor(Double magnitude) {
        if (magnitude >= 7.0) return "#d32f2f"; // Red
        if (magnitude >= 6.0) return "#f57c00"; // Orange
        if (magnitude >= 5.0) return "#fbc02d"; // Yellow
        return "#388e3c"; // Green
    }

    private static String getAlertEmoji(Double magnitude) {
        if (magnitude >= 7.0) return "🚨";
        if (magnitude >= 6.0) return "⚠️";
        if (magnitude >= 5.0) return "📢";
        return "ℹ️";
    }
}