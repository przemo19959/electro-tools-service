package pl.dabrowski.electrotools.elements.overcurrentprotection;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.dabrowski.electrotools.elements.basic.BasicElement;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.create.CreateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.read.ReadOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.elements.overcurrentprotection.service.update.UpdateOvercurrentProtectionElementDto;
import pl.dabrowski.electrotools.project.Project;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(name = "t_overcurrent_protection_elements")
@EntityListeners(value = AuditingEntityListener.class)
@Audited
public class OvercurrentProtectionElement extends BasicElement {
    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OvercurrentProtectionType type;

    @Column(name = "amperage")
    private int amperage;

    public static OvercurrentProtectionElement create(CreateOvercurrentProtectionElementDto dto, Project project) {
        final OvercurrentProtectionElement overcurrentProtectionElement = new OvercurrentProtectionElement();
        overcurrentProtectionElement.x = dto.getX();
        overcurrentProtectionElement.y = dto.getY();
        overcurrentProtectionElement.label = dto.getLabel();
        overcurrentProtectionElement.type = dto.getType();
        overcurrentProtectionElement.amperage = dto.getAmperage();
        overcurrentProtectionElement.project = project;

        return overcurrentProtectionElement;
    }

    public OvercurrentProtectionElement update(UpdateOvercurrentProtectionElementDto dto) {
        this.x = dto.getX();
        this.y = dto.getY();
        this.label = dto.getLabel();
        this.type = dto.getType();
        this.amperage = dto.getAmperage();
        return this;
    }

    public ReadOvercurrentProtectionElementDto toDto() {
        return ReadOvercurrentProtectionElementDto.builder()
            .id(id)
            .x(x)
            .y(y)
            .label(label)
            .type(type)
            .amperage(amperage)
            .build();
    }
}
