import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {Component, ElementRef, HostListener, Input, OnInit} from '@angular/core';
import {Option, OptionsService} from '../services/options.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'custommobs-option',
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule, NgClass, RouterLink],
  templateUrl: './option.component.html',
  styleUrl: './option.component.css'
})
export class OptionComponent implements OnInit {
  className: string = '';
  classDescription: string = '';
  options: Option[] = [];
  searchTerm: string = '';
  filteredOptions: any[] = [];
  searchResults: { entityType: string; options: string[] }[] = [];
  searching: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private optionsService: OptionsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.className = params['name'];

      this.optionsService.options$.subscribe((data: { [key: string]: Option[] }) => {
        if (data[this.className]) {
          this.options = data[this.className];
        } else {
          this.router.navigate(['/']);
        }
      });

      this.optionsService.entityTypes$.subscribe(entityMap => {
        const applicableEntities = Object.entries(entityMap)
          .filter(([entityType, entities]) =>
            entities.some(name => name.toLowerCase() === this.className.toLowerCase())
          )
          .map(([entityType]) => this.formatEntityName(entityType));

        if(applicableEntities.length == 1)
          this.classDescription = `Only the following mob is an instance of <b>${this.className}</b> : ${applicableEntities[0]}`;
        else
          this.classDescription = `All of the following mobs are instances of <b>${this.className}</b> : ${applicableEntities.join(', ')}`;
      });
    });
    this.filteredOptions = this.options;
  }

  onSearchFocus() {
    if (this.searchTerm?.trim()) {
      this.onSearchChange();
    }
  }

  @HostListener('document:click', ['$event'])
  onClick(event: MouseEvent) {
    const targetElement = event.target as HTMLElement;

    const clickedInsideInput = targetElement.closest('.searchbar') || targetElement.closest('.search-results');

    if (!clickedInsideInput) {
      this.searchResults = [];
    }

    this.searching = clickedInsideInput != null;
  }

  onSearchChange() {
    const term = this.searchTerm?.toLowerCase().trim() ?? '';
    if (term === '') {
      this.searchResults = [];
      return;
    }

    this.optionsService.entityTypes$.subscribe((entityMap: { [entityType: string]: string[] }) => {
      const entries = Object.entries(entityMap).map(([entityType, options]) => {
        const formattedType = entityType.toLowerCase().replaceAll('_', ' ');
        return { entityType, formattedType, options };
      });

      let filtered = [];

      // Search for anywhere in the text if under 7 results, otherwise only the start!
      filtered = entries.filter(entry => entry.formattedType.includes(term));
      if (filtered.length > 7) {
        filtered = entries.filter(entry => entry.formattedType.startsWith(term));
      }

      this.searchResults = filtered.map(({ entityType, options }) => ({
        entityType,
        options,
      }));
    });
  }

  resetSearch(): void {
    this.searchResults = [];
    this.searchTerm = '';
  }

  formatClassName(name: string): string {
    return (name ?? '').replace(/([a-z])([A-Z])/g, '$1 $2');
  }

  formatEntityName(name: string): string {
    if (!name) return '';
    return name
      .toLowerCase()
      .replace(/_/g, ' ')
      .replace(/\b\w/g, c => c.toUpperCase());
  }
}
