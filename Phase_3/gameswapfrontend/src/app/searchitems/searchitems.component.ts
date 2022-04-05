import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {SelectionModel} from "@angular/cdk/collections";
import { GameSwapItem } from '../models';
import { GameswapService } from '../gameswap.service';
import { Subscription } from 'rxjs';

const queryParamName = "pippo";

@Component({
  selector: 'app-searchitems',
  templateUrl: './searchitems.component.html',
  styleUrls: ['./searchitems.component.scss']
})
export class SearchitemsComponent implements OnInit, OnDestroy {

  
  item: GameSwapItem
  subscriptionItem: Subscription;
  keyword
  miles
  inpostal
  searchSelected
  displayedColumns = ['Item #', 'Game Type', 'Title', 'Condition', 'Description', 'Distance', 'Details'];
  dataSource
  selection: SelectionModel<GameSwapItem> = new SelectionModel<GameSwapItem>(false, []);
  
  constructor(private router:Router,
    private gameswapService: GameswapService) {

  }

  ngOnInit():void{

    this.subscriptionItem = this.gameswapService.currentItem.subscribe(item => this.item = this.item)


    ELEMENT_DATA.splice(0,ELEMENT_DATA.length);   

    ELEMENT_DATA.push(
      {
        itemId: 1,
        type: 'video game',
        title: 'tetris',
        condition: 'good',
        description: '',
        distance: 0.0,
        details: 'details',
        selected: false
      })   
      ELEMENT_DATA.push(
        {
          itemId: 2,
          type: 'jigsaw puzzle',
          title: 'Georgia Tech',
          condition: 'mint',
          description: 'blah blah',
          distance: 10.8,
          details: 'details',
          selected: false
        })   
           
    this.dataSource = ELEMENT_DATA;
  }

  ngOnDestroy() {
    this.subscriptionItem.unsubscribe()
  }

  selectRow($event: any, row:Element){
    console.info("clicked", $event);
    console.info(row); 
  }

  onBackToMain() {
    this.router.navigate(['/Welcome']);
  }  
 
  searchString: string;

  onSearch() {
    console.log(this.searchSelected)
    if(this.searchSelected == 'Bykeyword')
    {        
      this.searchString = 'Keyword "' + this.keyword + '"'
    }
    else if(this.searchSelected == 'Bymypostal')
    {
      this.searchString = 'My Postal' 
    }
    else if(this.searchSelected == 'Bymiles')
    {
      this.searchString = 'Within "' + this.miles + '" miles'
    }
    else if(this.searchSelected == 'Byinpostal')
    {
      this.searchString = 'In Postal "' + this.inpostal + '"'
    }
    
    console.log(this.searchString)
  }

  onDetails(element) {
    //console.log(element)    
    this.item = element
    console.log(this.item)    
    this.gameswapService.updateSelectedItem(this.item);
    this.router.navigate(['/ViewItem']);
  }
 
}

const ELEMENT_DATA: GameSwapItem[] = [ 
];
