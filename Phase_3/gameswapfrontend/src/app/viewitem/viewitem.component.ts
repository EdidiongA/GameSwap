import { Component, OnInit, OnDestroy } from '@angular/core';
import { GameswapService } from '../gameswap.service';
import { GameSwapItem } from '../models';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-viewitem',
  templateUrl: './viewitem.component.html',
  styleUrls: ['./viewitem.component.scss']
})
export class ViewitemComponent implements OnInit, OnDestroy {

  constructor(private gameswapService: GameswapService) { }

  item: GameSwapItem
  subscriptionItem: Subscription;
  ngOnInit(): void {
    
  this.subscriptionItem = this.gameswapService.currentItem.subscribe(item => this.item = item)

  console.log('In view item')
  console.log(this.item)
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  onProposeSwap() {
   
  }

}
