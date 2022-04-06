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
  loginError
  ngOnInit(): void {
  }

  onLogin() {
    //this.gameswapService.addUser()
    const promise = this.gameswapService.getAuthentication(this.loginId, this.password)
    promise.then((data)=>{
      console.log(JSON.stringify(data));
      var status = JSON.stringify(data)
      
      if(status == 'true')
      {
        this.gameswapService.updateUserId(this.loginId)
        this.router.navigate(['/Welcome']);
      }
      else
        this.loginError = 'Login failed. Please try again.'
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });
  }

  onRegister() {
    this.router.navigate(['/Register']);
  }
}
