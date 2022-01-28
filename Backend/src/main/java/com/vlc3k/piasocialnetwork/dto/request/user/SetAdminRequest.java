package com.vlc3k.piasocialnetwork.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetAdminRequest {
    private boolean adminRole;
}
