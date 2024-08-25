import {
  Component,
  Output,
  EventEmitter,
  Input,
  ViewEncapsulation,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/services/Auth';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  encapsulation: ViewEncapsulation.None,
})
export class HeaderComponent {
  @Input() showToggle = true;
  @Input() toggleChecked = false;
  @Output() toggleMobileNav = new EventEmitter<void>();
  @Output() toggleMobileFilterNav = new EventEmitter<void>();
  @Output() toggleCollapsed = new EventEmitter<void>();

  showFiller = false;
  iduser:any
  user:any
  constructor(public dialog: MatDialog ,private auth: AuthService) {
    
    this.iduser = localStorage.getItem('UserId');
    this.getUserProfileFromDatabase();
  }
  getUserProfileFromDatabase() {
    const userId = localStorage.getItem('UserId');
    if (userId !== null && userId !== undefined && userId !== 'undefined') {
      this.auth.getUser(userId).subscribe((user) => { 
        this.user = user;
        console.log(this.user.fullName); // Log the user's full name
      });
    }
  }
   
  logout() {
    this.auth.signOut();
  }
  }

