import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {

  constructor(private router:Router) { }
  name
  myrating
  unacceptedSwaps
  unratedSwaps
  ngOnInit(): void {
    this.name = '[need to name get from DB]'
    this.myrating = '[need to rating get from DB]'
    this.unacceptedSwaps = '[need to unaccepted swaps get from DB]'
    this.unratedSwaps = '[need to unrated swaps get from DB]'    
    
  }

  onListItem() {
    this.router.navigate(['/ListItem']);
  }
  onMyItems() {
    this.router.navigate(['/MyItem']);
  }
  onSearchItems() {
    this.router.navigate(['/SearchItem']);
  }
  onSwapHistory() {
    this.router.navigate(['/SwapHistory']);
  }
  onUpdateInfo() {
    this.router.navigate(['/UpdateInfo']);
  }
  onLogout() {
    this.router.navigate(['/Login']);
  }

}
