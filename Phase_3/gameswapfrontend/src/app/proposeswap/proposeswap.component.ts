import { Component, OnInit, OnDestroy } from '@angular/core';
import {SelectionModel} from "@angular/cdk/collections";
import { GameSwapItem } from '../models';
import { Router } from '@angular/router';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

const queryParamName = "pippo";

@Component({
  selector: 'app-proposeswap',
  templateUrl: './proposeswap.component.html',
  styleUrls: ['./proposeswap.component.scss']
})
export class ProposeswapComponent implements OnInit {

  
  userId: String
  subscriptionUser: Subscription;
  
  swapItem
  constructor(private router:Router,
    private gameswapService: GameswapService) { }

  displayedColumns = ['Item #', 'Game Type', 'Title', 'Condition', 'selection'];
  dataSource
  selection: SelectionModel<GameSwapItem> = new SelectionModel<GameSwapItem>(false, []);
  ngOnInit():void{

    ELEMENT_DATA.splice(0,ELEMENT_DATA.length);
    this.subscriptionUser = this.gameswapService.currentUser.subscribe(user => this.userId = user)

    const promise = this.gameswapService.getItemForSwap(this.userId)
    promise.then((data)=>{
      console.log(JSON.stringify(data));
      data.forEach(element => {
        ELEMENT_DATA.push(
          {
            itemId: element.id,
            type: 'video game',
            title: element.name,
            condition: element.condition,
            description: element.description,
            distance: 0,
            details: '',
            selected: false
          })
      });                  
      this.dataSource = ELEMENT_DATA;
      //this.name = data.firstName + ' ' + data.lastName
      //this.myrating = data.userStats.rating
      //this.unacceptedSwaps = data.userStats.unacceptedSwapCount
      //this.unratedSwaps = data.userStats.unratedSwapCount
    
    }).catch((error)=>{
      console.log("Promise rejected with " + JSON.stringify(error));
    });  

    // ELEMENT_DATA.push(
    //   {
    //     itemId: 1,
    //     type: 'video game',
    //     title: 'tetris',
    //     condition: 'good',
    //     description: '',
    //     distance: 0,
    //     details: '',
    //     selected: false
    //   })
    // ELEMENT_DATA.push(
    //   {
    //     itemId: 2,
    //     type: 'board game',
    //     title: 'monopoly',
    //     condition: 'very good',
    //     description: '',
    //     distance: 0,
    //     details: '',
    //     selected: false
    //   })
    // ELEMENT_DATA.push(
    //   {
    //     itemId: 3,
    //     type: 'card game',
    //     title: 'uno',
    //     condition: 'fair',
    //     description: '',
    //     distance: 0,
    //     details: '',
    //     selected: false
    //   })
           
    
  }

  ngOnDestroy(): void {
    this.subscriptionUser.unsubscribe()
  }

  selectRow($event: any, row:GameSwapItem){
    console.info("clicked", $event);
    console.info(row);
    $event.preventDefault();
        if (!row.selected) {
            this.dataSource.forEach((row) => row.selected = false);
            row.selected = true;
            this.selection.select(row);
            if (location.href.indexOf(queryParamName) >= 0) {
                location.href = location.href.replace(queryParamName, "");
            }
        }
  }

}

// export interface Element {
//   itemId: number;
//   type: string;
//   title: string;
//   condition: string;
//   selected: boolean;
// }

const ELEMENT_DATA: GameSwapItem[] = [ 
];