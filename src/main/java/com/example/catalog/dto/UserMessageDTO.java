package com.example.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDTO {

    //<editor-fold desc="User Message Contants">
    public static final String SEVERITY_ERROR = "danger";
    public static final String SEVERITY_WARN = "warning";
    public static final String SEVERITY_INFO = "info";
    public static final String SEVERITY_SUCCESS = "success";
    //</editor-fold>

    private String message;
    private String severity;

    public static UserMessageDTO createError(final String message) {
        UserMessageDTO dto = new UserMessageDTO();
        dto.setSeverity(SEVERITY_ERROR);
        dto.setMessage(message);
        return dto;
    }

    public static UserMessageDTO createSuccess(final String message) {
        UserMessageDTO dto = new UserMessageDTO();
        dto.setSeverity(SEVERITY_SUCCESS);
        dto.setMessage(message);
        return dto;
    }
}
