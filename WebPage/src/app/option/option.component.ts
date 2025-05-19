import {ActivatedRoute} from '@angular/router';
import {NgForOf, NgIf} from '@angular/common';
import {Component, Input, OnInit} from '@angular/core';
import {Option, OptionsService} from '../services/options.service';

@Component({
  selector: 'custommobs-option',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './option.component.html',
  styleUrl: './option.component.css'
})
export class OptionComponent implements OnInit {
  className: string = '';
  options: Option[] = [];

  constructor(
    private route: ActivatedRoute,
    private optionsService: OptionsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.className = params['name'];
      this.optionsService.options$.subscribe((data: { [key: string]: Option[] }) => {
        this.options = data[this.className] || [];
      });
    });
  }

  formatClassName(name: string): string {
    return (name ?? '').replace(/([a-z])([A-Z])/g, '$1 $2');
  }
}
