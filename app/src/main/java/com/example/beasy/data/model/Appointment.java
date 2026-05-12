package com.example.beasy.data.model;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(
        tableName = "appointments",
        foreignKeys = @ForeignKey(
                entity      = UserEntity.class,
                parentColumns = "id",
                childColumns  = "userId",
                onDelete    = ForeignKey.CASCADE
        ),
        indices = { @Index("userId") }
)
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;             // which user made this booking
    public String professionalName;   // e.g. "Master Sparky Electrics"
    public String category;           // e.g. "Electrical"
    public String price;              // e.g. "Starts $85/hr"
    public String date;               // "yyyy-MM-dd"
    public String time;               // "11:30 AM"
    public String description;        // user's issue description
    public String status;             // "UPCOMING" | "COMPLETED" | "CANCELLED"

    public Appointment(int userId, String professionalName, String category,
                       String price, String date, String time,
                       String description, String status) {
        this.userId = userId;
        this.professionalName = professionalName;
        this.category = category;
        this.price = price;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
    }
}