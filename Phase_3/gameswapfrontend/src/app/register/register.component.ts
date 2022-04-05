import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';

interface Type {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  constructor(private router:Router,
    private gameswapService: GameswapService,) { }

  email
  nickname
  password
  city
  firstName
  state
  lastName
  phoneNumber
  type
  isShowPhoneNumberChecked

  postalCodeFilterControl = new FormControl();
  postalCodes: string[] = [];
  filteredPostalCodes: Observable<string[]>;

  types: Type[] = [
    {value: 'Home', viewValue: 'Home'},
    {value: 'Work', viewValue: 'Work'},
    {value: 'Mobile', viewValue: 'Mobile'}
  ];

  ngOnInit(): void {
    this.gameswapService.getPostalCodes().then((response: string[]) => {
      this.postalCodes = response;

      this.filteredPostalCodes = this.postalCodeFilterControl.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value))
      );
    });
  }

  onRegister() {
    console.log(this.email);
    console.log(this.nickname);
    console.log(this.password);
    console.log(this.city);
    console.log(this.firstName);
    console.log(this.state);
    console.log(this.lastName);
    console.log(this.postalCodeFilterControl.value);
    console.log(this.phoneNumber);
    console.log(this.type);
    console.log(this.isShowPhoneNumberChecked);

    this.router.navigate(['/Welcome']);
  }

  private _filter(value: string): string[] {
    return this.postalCodes.filter(postalCode => postalCode.indexOf(value) === 0);
  }
}
