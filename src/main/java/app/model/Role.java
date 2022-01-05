package app.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Table("roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Role implements GrantedAuthority {

    @Id
    private int role_id;
    private String role_name;

    @Override
    public String getAuthority() {
        return getRole_name();
    }

    @Override
    public String toString() {
        return getRole_name();
    }
}
