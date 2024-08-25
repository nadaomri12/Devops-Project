import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-image-upload',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css'], // Corrected from styleUrl to styleUrls
})
export class ImageUploadComponent {
  imageSrc: string = '';

  myForm = new FormGroup({
    file: new FormControl('', [Validators.required]),
    fileSource: new FormControl('', [Validators.required]),
  });

  constructor(private http: HttpClient) {}

  get f() {
    return this.myForm.controls;
  }

  onFileChange(event: any) {
    const reader = new FileReader();

    if (event.target.files && event.target.files.length) {
      const [file] = event.target.files;
      reader.readAsDataURL(file);

      reader.onload = () => {
        this.imageSrc = reader.result as string;

        this.myForm.patchValue({
          fileSource: file,
        });
      };
    }
  }

  submit() {
    const formData: FormData = new FormData();
    formData.append('file', this.myForm.get('fileSource')!.value!);

    this.http.post('http://localhost:8080/api/upload', formData, { responseType: 'text' }).subscribe(
      (res) => {
        console.log(res);
        
      },
      (err) => {
        console.error(err);
        
      }
    );
  }
}