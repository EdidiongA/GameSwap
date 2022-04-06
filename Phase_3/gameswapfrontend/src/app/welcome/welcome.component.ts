import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit, OnDestroy {

  
  userId: String
  subscriptionUser: Subscription;
    
  constructor(private router:Router,
    private gameswapService: GameswapService) 
    { }

  name
  myrating
  unacceptedSwaps
  unratedSwaps
  ngOnInit(): void {

    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user)

    const promise = this.gameswapService.getUserInfo(this.userId)
    promise.then((data)=>{
      console.log(JSON.stringify(data));
                  
      this.name = data.firstName + ' ' + data.lastName
      this.myrating = data.userStats.rating
      this.unacceptedSwaps = data.userStats.unacceptedSwapCount
      this.unratedSwaps = data.userStats.unratedSwapCount
    
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  
    
  }

  ngOnDestroy(): void {
    this.subscriptionUser.unsubscribe()
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
    this.gameswapService.updateUserId('')
    this.router.navigate(['/Login']);
  }

}
