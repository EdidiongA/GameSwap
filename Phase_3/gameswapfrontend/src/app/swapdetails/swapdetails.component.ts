import { Component, OnInit } from '@angular/core';



@Component({
  selector: 'app-swapdetails',
  templateUrl: './swapdetails.component.html',
  styleUrls: ['./swapdetails.component.scss']
})
export class SwapdetailsComponent implements OnInit {

  constructor() { }
  //swap details
  proposed
  acceptedRejected
  status
  myRole
  ratingLeft
  //user details
  Nickname
  Distance
  Name
  Email
  Phone
  //Proposed item
  itemNo
  title
  gameType
  condition
  description
//desired item
desiredItemNo
desiredTitle
desiredGameType
desiredCondition

  ngOnInit(): void {

    this.status = "[Accepted]"
  }
  
}
