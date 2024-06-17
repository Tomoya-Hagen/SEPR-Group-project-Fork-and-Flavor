import {Component, Input, OnInit, input, EventEmitter, Output} from '@angular/core';
import { faStar } from '@fortawesome/free-solid-svg-icons/faStar'

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.scss'
})
export class StarRatingComponent implements OnInit {
  faStar=faStar;
  ngOnInit(): void {
  }

  @Input()
  disabled = false;

  @Input()
  value = 0;

  @Output()
  valueEvent = new EventEmitter();



  setRating(value: number){
    if(!this.disabled) {
      this.value = value;
      this.valueEvent.emit(this.value);
    }
  }
}
