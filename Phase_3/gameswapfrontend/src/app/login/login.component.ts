import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private router:Router,
    private gameswapService: GameswapService,) { }

  loginId
  password

  ngOnInit(): void {
  }

  onLogin() {
    //this.gameswapService.addUser()
    console.log(this.loginId + ',' + this.password)
    this.router.navigate(['/Welcome']);
  }

  onRegister() {
    this.router.navigate(['/Register']);
  }
}
