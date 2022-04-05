import { Component, OnInit } from '@angular/core';
import {SelectionModel} from "@angular/cdk/collections";
import { GameSwapItem } from '../models';

const queryParamName = "pippo";

@Component({
  selector: 'app-proposeswap',
  templateUrl: './proposeswap.component.html',
  styleUrls: ['./proposeswap.component.scss']
})
export class ProposeswapComponent implements OnInit {

  
  proposeItem
  constructor() { }

  displayedColumns = ['Item #', 'Game Type', 'Title', 'Condition', 'selection'];
  dataSource
  selection: SelectionModel<GameSwapItem> = new SelectionModel<GameSwapItem>(false, []);
  ngOnInit():void{

    ELEMENT_DATA.push(
      {
        itemId: 1,
        type: 'video game',
        title: 'tetris',
        condition: 'good',
        description: '',
        distance: 0,
        details: '',
        selected: false
      })
    ELEMENT_DATA.push(
      {
        itemId: 2,
        type: 'board game',
        title: 'monopoly',
        condition: 'very good',
        description: '',
        distance: 0,
        details: '',
        selected: false
      })
    ELEMENT_DATA.push(
      {
        itemId: 3,
        type: 'card game',
        title: 'uno',
        condition: 'fair',
        description: '',
        distance: 0,
        details: '',
        selected: false
      })
           
    this.dataSource = ELEMENT_DATA;
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