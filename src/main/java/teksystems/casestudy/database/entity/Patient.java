package teksystems.casestudy.database.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "patients")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;

    @NonNull
    @Column(name = "user_id")
    private Integer userId;

    @NonNull
    @Column(name = "medical_record_number")
    private Integer medicalRecordNumber;

//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
    @NonNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NonNull
    @Column(name = "sex")
    private String sex;

    @Column(name = "gender")
    private String gender;

    @Column(name = "pronouns")
    private String pronouns;

    @Column(name = "preferred_name")
    private String preferredName;

    @Column(name = "primary_language")
    private String primaryLanguage;
    //private List<String> primaryLanguage = new ArrayList<>();  //THIS SHOULD BE A LIST, since people can have multiple languages

    @Column(name = "problem_list")
    private String problemList;

    //    @Column(name = "problem_list")
//    private List<Problem> problemList;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}
