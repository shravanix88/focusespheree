package com.focussphere.service;

import com.focussphere.model.Notification;
import com.focussphere.model.NotificationType;
import com.focussphere.model.Room;
import com.focussphere.model.User;
import com.focussphere.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = false)
    public Notification createNotification(User recipient, NotificationType type, String title, String message, Room room, User sender) {
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }
        if (type == null || title == null || message == null) {
            throw new IllegalArgumentException("Type, title, and message are required");
        }

        try {
            Notification notification = new Notification(recipient, type, title, message, room, sender);
            return notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Error creating notification: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Notification> getNotificationsForUser(User user) {
        if (user == null) {
            return new ArrayList<>();
        }
        try {
            List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
            if (notifications == null) {
                return new ArrayList<>();
            }
            
            List<Notification> result = new ArrayList<>();
            for (Notification n : notifications) {
                try {
                    // Ensure all required fields are set
                    if (n == null) continue;
                    
                    if (n.getIsRead() == null) {
                        n.setIsRead(Boolean.FALSE);
                    }
                    if (n.getCreatedAt() == null) {
                        n.setCreatedAt(LocalDateTime.now());
                    }
                    if (n.getTitle() == null) {
                        n.setTitle("Notification");
                    }
                    if (n.getMessage() == null) {
                        n.setMessage("");
                    }
                    
                    result.add(n);
                } catch (Exception e) {
                    System.err.println("Error processing notification: " + e.getMessage());
                }
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error retrieving notifications for user: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Notification> getUnreadNotificationsForUser(User user) {
        if (user == null) {
            return new ArrayList<>();
        }
        try {
            return notificationRepository.findByRecipientAndIsReadOrderByCreatedAtDesc(user, false);
        } catch (Exception e) {
            System.err.println("Error retrieving unread notifications: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public long getUnreadNotificationCount(User user) {
        if (user == null) {
            return 0;
        }
        try {
            return notificationRepository.countByRecipientAndIsRead(user, false);
        } catch (Exception e) {
            System.err.println("Error counting unread notifications: " + e.getMessage());
            return 0;
        }
    }

    @Transactional(readOnly = false)
    public Notification markAsRead(Long notificationId) {
        if (notificationId == null) {
            return null;
        }
        try {
            Notification notification = notificationRepository.findById(notificationId).orElse(null);
            if (notification != null) {
                notification.setIsRead(true);
                return notificationRepository.save(notification);
            }
        } catch (Exception e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void deleteNotification(Long notificationId) {
        if (notificationId == null) {
            return;
        }
        try {
            notificationRepository.deleteById(notificationId);
        } catch (Exception e) {
            System.err.println("Error deleting notification: " + e.getMessage());
        }
    }

    public Notification getNotification(Long notificationId) {
        if (notificationId == null) {
            return null;
        }
        try {
            return notificationRepository.findById(notificationId).orElse(null);
        } catch (Exception e) {
            System.err.println("Error retrieving notification: " + e.getMessage());
            return null;
        }
    }
}
