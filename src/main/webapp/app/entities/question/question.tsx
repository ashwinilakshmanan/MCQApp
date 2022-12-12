import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuestion } from 'app/shared/model/question.model';
import { getEntities } from './question.reducer';

export const Question = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const questionList = useAppSelector(state => state.question.entities);
  const loading = useAppSelector(state => state.question.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="question-heading" data-cy="QuestionHeading">
        <Translate contentKey="mcqApp.question.home.title">Questions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mcqApp.question.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/question/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mcqApp.question.home.createLabel">Create new Question</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {questionList && questionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="mcqApp.question.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="mcqApp.question.question">Question</Translate>
                </th>
                <th>
                  <Translate contentKey="mcqApp.question.answer">Answer</Translate>
                </th>
                <th>
                  <Translate contentKey="mcqApp.question.category">Category</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {questionList.map((question, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/question/${question.id}`} color="link" size="sm">
                      {question.id}
                    </Button>
                  </td>
                  <td>{question.question}</td>
                  <td>{question.answer}</td>
                  <td>{question.category ? <Link to={`/category/${question.category.id}`}>{question.category.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/question/${question.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/question/${question.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/question/${question.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="mcqApp.question.home.notFound">No Questions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Question;
