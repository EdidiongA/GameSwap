import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {SelectionModel} from "@angular/cdk/collections";
import { SwapItem } from '../models';
import { GameSwapItem } from '../models';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

const queryParamName = "pippo";

@Component({
  selector: 'app-acknowledgeswap',
  templateUrl: './acknowledgeswap.component.html',
  styleUrls: ['./acknowledgeswap.component.scss']
})
export class AcknowledgeswapComponent implements OnInit, OnDestroy {

  
  item: GameSwapItem
  subscriptionItem: Subscription;
  displayedColumns = ['Date', 'Desired Item', 'Proposer', 'Rating', 'Distance', 'Proposed Item', 'Ack'];
  dataSource
  selection: SelectionModel<SwapItem> = new SelectionModel<SwapItem>(false, []);
  
  constructor(private router:Router,
    private gameswapService: GameswapService) { }

  ngOnInit(): void {
    this.subscriptionItem = this.gameswapService.currentItem.subscribe(item => this.item = this.item)

    ELEMENT_DATA.splice(0,ELEMENT_DATA.length);   

    ELEMENT_DATA.push(
      {
        swapId: 10,
        date: '1/15/2020',
        desiredItemId: '101',
        desiredItemTitle: 'Cards Against Humanity',
        proposer:'HeroOfTime',
        rating:4.99,
        distance:8.2,
        proposedItemId:103,
        proposedItemTittle:'super mario'
      })   
      ELEMENT_DATA.push(
        {
          swapId: 101,
          date: '1/25/2020',
          desiredItemId: '108',
          desiredItemTitle: 'Against Humanity',
          proposer:'Hero',
          rating:4.25,
          distance:108.2,
          proposedItemId:1031,
          proposedItemTittle:'super fighter'
        })   
    this.dataSource = ELEMENT_DATA;
  }

  ngOnDestroy() {
    
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }  
 
  onAccept(row: SwapItem) {
    console.log('Accept - ' + row)        
  }
  onReject(row: SwapItem) {
    console.log('Reject - ' + row)
  }

  onDesiredItemDetails(row) {

    this.item = {
      itemId: row.desiredItemId,
      type: 'video game',
      title: row.desiredItemTitle,
      condition: 'good',
      description: '',
      distance: row.distance,
      details: 'details',
      selected: false
    }
    console.log(this.item)    
    this.gameswapService.updateSelectedItem(this.item);
    this.router.navigate(['/ViewItem']);
  }
    onProposedItemDetails(row) {
  
      this.item = {
        itemId: row.proposedItemId,
        type: 'video game',
        title: row.proposedItemTittle,
        condition: 'good',
        description: '',
        distance: row.distance,
        details: 'details',
        selected: false
      }
  
    console.log(this.item)    
    this.gameswapService.updateSelectedItem(this.item);
    this.router.navigate(['/ViewItem']);
  }
}

const ELEMENT_DATA: SwapItem[] = [ 
];
