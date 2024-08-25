import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/dialog/dialog.component';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class AppSideLoginComponent implements OnInit {
  loginForm!: FormGroup;
  hide = true;  // This property controls the visibility of the password
  isFormSubmitted: boolean = false;
  constructor(
    private fb: FormBuilder,
    private auth:AuthService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]+$/)]],
     
      password: ['', Validators.required]
    });
  }

  togglePasswordVisibility() {
    this.hide = !this.hide;
  }

  onLogin() {
    if (this.loginForm.valid) {
      const userObject = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password
      };

      this.auth.login(userObject).subscribe({
        next: (res) => {
          localStorage.setItem('userRole', res.role);
          localStorage.setItem('UserId', res.userId); 
          localStorage.setItem('token',res.token)
          this.loginForm.reset();
          this.router.navigate(['/ui-components/Company']);
        },
        error: (err) => {
          this.dialog.open(DialogComponent, {
            data: {
              title: 'Sorry',
              message: 'Mot de passe incorrect'
            }
          });
       
        }
      });
    }
  }
}
