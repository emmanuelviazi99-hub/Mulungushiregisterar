package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import zm.ac.mulungushi.registrar.data.model.Student;

/** A student as the server sends and receives it (JSON field names match the API contract). */
public class StudentDto {

    @SerializedName("studentId") public String studentId;
    @SerializedName("studentNumber") public String studentNumber;
    @SerializedName("name") public String name;
    @SerializedName("programme") public String programme;
    /** G01..G04 or null for Unassigned. */
    @SerializedName("group") public String group;
    @SerializedName("active") public boolean active = true;
    @SerializedName("version") public int version;
    @SerializedName("email") public String email;
    @SerializedName("phone") public String phone;
    @SerializedName("nrc") public String nrc;
    /** Sent with an edit so the server can detect a conflicting change. */
    @SerializedName("baseVersion") public Integer baseVersion;

    public Student toModel() {
        Student s = new Student(studentId, name, studentNumber, programme,
                group == null ? "Unassigned" : group, active);
        s.email = email;
        s.phone = phone;
        s.nrc = nrc;
        return s;
    }

    public static StudentDto fromModel(Student s) {
        StudentDto d = new StudentDto();
        d.studentId = s.id;
        d.studentNumber = s.number;
        d.name = s.name;
        d.programme = s.programme;
        d.group = "Unassigned".equals(s.group) ? null : s.group;
        d.active = s.active;
        d.email = s.email;
        d.phone = s.phone;
        d.nrc = s.nrc;
        return d;
    }
}
