package it.library.commondata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "Information")
public class MongoInformation implements Serializable {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field
    private String type;

    @Field
    private Object payload;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime dtInsert;
}
